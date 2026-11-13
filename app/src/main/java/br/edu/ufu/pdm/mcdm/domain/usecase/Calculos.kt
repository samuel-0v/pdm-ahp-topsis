package br.edu.ufu.pdm.mcdm.domain.usecase

import br.edu.ufu.pdm.mcdm.domain.engine.AhpEngine
import br.edu.ufu.pdm.mcdm.domain.engine.FalhaMatematica
import br.edu.ufu.pdm.mcdm.domain.engine.PoliticaRanking
import br.edu.ufu.pdm.mcdm.domain.engine.SaidaTopsis
import br.edu.ufu.pdm.mcdm.domain.engine.TopsisEngine
import br.edu.ufu.pdm.mcdm.domain.model.Alternativa
import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.DetalheRanking
import br.edu.ufu.pdm.mcdm.domain.model.Distancias
import br.edu.ufu.pdm.mcdm.domain.model.Julgamento
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.ParCriterios
import br.edu.ufu.pdm.mcdm.domain.model.ResultadoAhp
import br.edu.ufu.pdm.mcdm.domain.model.ResultadoTopsis
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import br.edu.ufu.pdm.mcdm.domain.validation.ValidadorValor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/** Problema que bloqueia um cálculo, localizado quando possível. */
data class Problema(val mensagem: String, val alternativaId: String? = null, val criterioId: String? = null)

sealed interface ResultadoCalculo<out T> {
    data class Sucesso<T>(val valor: T) : ResultadoCalculo<T>

    /** Pré-condições não atendidas: nada é publicado como resultado. */
    data class Bloqueado(val problemas: List<Problema>) : ResultadoCalculo<Nothing>

    /** Falha matemática: não é conclusão nem “sem diferenciação”. */
    data class Falha(val problema: Problema) : ResultadoCalculo<Nothing>
}

/** Monta a matriz recíproca na ordem dos critérios preservados e executa o AHP fora da Main. */
class CalcularAhp(private val dispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(
        criterios: List<CriterioDefinicao>,
        julgamentos: Map<ParCriterios, Julgamento>,
    ): ResultadoCalculo<ResultadoAhp> {
        val ordenados = criterios.sortedBy { it.ordem }
        val faltantes = mutableListOf<Problema>()
        for (i in ordenados.indices) for (j in i + 1 until ordenados.size) {
            val par = ParCriterios.de(ordenados[i].id, ordenados[j].id)
            if (par !in julgamentos) faltantes += Problema("Par pendente: ${ordenados[i].nome} × ${ordenados[j].nome}.")
        }
        if (faltantes.isNotEmpty()) return ResultadoCalculo.Bloqueado(faltantes)
        val ids = ordenados.map { it.id }
        if (julgamentos.keys.any { it.menorId !in ids || it.maiorId !in ids }) {
            return ResultadoCalculo.Bloqueado(listOf(Problema("Julgamento referencia critério externo à avaliação.")))
        }

        return withContext(dispatcher) {
            val n = ordenados.size
            val matriz = List(n) { i ->
                List(n) { j ->
                    when {
                        i == j -> 1.0
                        else -> {
                            val julg = julgamentos.getValue(ParCriterios.de(ids[i], ids[j]))
                            if (julg.par.menorId == ids[i]) julg.valorMenorSobreMaior else 1.0 / julg.valorMenorSobreMaior
                        }
                    }
                }
            }
            try {
                val s = AhpEngine.calcular(matriz)
                ResultadoCalculo.Sucesso(ResultadoAhp(ids.zip(s.pesos).toMap(), s.lambdaMax, s.ic, s.ri, s.rc))
            } catch (f: FalhaMatematica) {
                ResultadoCalculo.Falha(Problema(f.message ?: "Falha no cálculo AHP."))
            }
        }
    }
}

/** Matriz e mapas índice ↔ ID preparados e validados para o TOPSIS. */
data class EntradaTopsis(
    val alternativaIds: List<String>,
    val criterioIds: List<String>,
    val matriz: List<List<Double>>,
    val pesos: List<Double>,
    val beneficio: List<Boolean>,
)

object PreparacaoTopsis {
    /**
     * Valida as entradas e converte para Double: centavos.toDouble() / 100.0 (sem divisão inteira).
     * Bloqueia: menos de duas alternativas, identidade duplicada, valor ausente/pendente/inválido,
     * AHP ausente/inconsistente e colisão monetária na conversão (DEC-041).
     */
    fun preparar(
        criterios: List<CriterioDefinicao>,
        alternativas: List<Alternativa>,
        ahp: ResultadoAhp?,
    ): ResultadoCalculo<EntradaTopsis> {
        val problemas = mutableListOf<Problema>()
        val ordenados = criterios.sortedBy { it.ordem }
        val alts = alternativas.sortedBy { it.ordem }
        if (alts.size < 2) problemas += Problema("Selecione pelo menos duas alternativas.")
        if (alts.map { it.chaveOrigem }.toSet().size != alts.size) problemas += Problema("Alternativa repetida (mesma origem e ID).")
        when {
            ahp == null -> problemas += Problema("Calcule os pesos AHP antes de processar.")
            !ahp.consistente -> problemas += Problema("AHP inconsistente (RC > 0,10): revise os julgamentos.")
            ordenados.any { ahp.pesos[it.id] == null } -> problemas += Problema("Pesos AHP não correspondem aos critérios.")
        }

        for (a in alts) for (c in ordenados) {
            val v = a.valores[c.id]
            when {
                v?.entradaPendente != null -> problemas += Problema("${a.identificacao.nome} · ${c.nome}: entrada inválida pendente.", a.alternativaId, c.id)
                v?.efetivo == null -> problemas += Problema("${a.identificacao.nome} · ${c.nome}: valor não informado.", a.alternativaId, c.id)
                else -> ValidadorValor.validar(v.efetivo, c.validacao)?.let {
                    problemas += Problema("${a.identificacao.nome} · ${c.nome}: $it", a.alternativaId, c.id)
                }
            }
        }
        if (problemas.isNotEmpty()) return ResultadoCalculo.Bloqueado(problemas)

        val matriz = alts.map { a -> ordenados.map { c -> paraDouble(a.valores.getValue(c.id).efetivo!!) } }

        // Colisão de resolução: valores monetários distintos que viram o mesmo Double.
        for ((j, c) in ordenados.withIndex()) {
            val porDouble = HashMap<Double, Long>()
            for ((i, a) in alts.withIndex()) {
                val canonico = (a.valores.getValue(c.id).efetivo as? ValorCanonico.Monetario)?.centavos ?: continue
                val anterior = porDouble.put(matriz[i][j], canonico)
                if (anterior != null && anterior != canonico) {
                    return ResultadoCalculo.Bloqueado(
                        listOf(Problema("${c.nome}: valores monetários distintos perdem distinção na conversão numérica.", criterioId = c.id)),
                    )
                }
            }
        }

        return ResultadoCalculo.Sucesso(
            EntradaTopsis(
                alternativaIds = alts.map { it.alternativaId },
                criterioIds = ordenados.map { it.id },
                matriz = matriz,
                pesos = ordenados.map { ahp!!.pesos.getValue(it.id) },
                beneficio = ordenados.map { it.natureza == Natureza.BENEFICIO },
            ),
        )
    }

    fun paraDouble(v: ValorCanonico): Double = when (v) {
        is ValorCanonico.Monetario -> v.centavos.toDouble() / 100.0
        is ValorCanonico.Numero -> v.valor
    }
}

/** Executa TOPSIS e aplica a política de empates; resultado por identidade da alternativa. Não grava. */
class ProcessarAvaliacao(private val dispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(
        criterios: List<CriterioDefinicao>,
        alternativas: List<Alternativa>,
        ahp: ResultadoAhp?,
    ): ResultadoCalculo<ResultadoTopsis> {
        val entrada = when (val p = PreparacaoTopsis.preparar(criterios, alternativas, ahp)) {
            is ResultadoCalculo.Sucesso -> p.valor
            is ResultadoCalculo.Bloqueado -> return p
            is ResultadoCalculo.Falha -> return p
        }
        return withContext(dispatcher) {
            try {
                when (val s = TopsisEngine.calcular(entrada.matriz, entrada.pesos, entrada.beneficio)) {
                    is SaidaTopsis.Definido -> {
                        val posicoes = PoliticaRanking.posicoes(s.coeficientes)
                        ResultadoCalculo.Sucesso(
                            ResultadoTopsis.Ranking(
                                entrada.alternativaIds.indices.associate { i ->
                                    entrada.alternativaIds[i] to DetalheRanking(s.dMais[i], s.dMenos[i], s.coeficientes[i], posicoes[i])
                                },
                            ),
                        )
                    }
                    is SaidaTopsis.SemDiferenciacao -> ResultadoCalculo.Sucesso(
                        ResultadoTopsis.SemDiferenciacao(
                            entrada.alternativaIds.indices.associate { i -> entrada.alternativaIds[i] to Distancias(s.dMais[i], s.dMenos[i]) },
                        ),
                    )
                }
            } catch (f: FalhaMatematica) {
                val criterioId = f.indiceCriterio?.let { entrada.criterioIds[it] }
                val nome = criterioId?.let { id -> criterios.firstOrNull { it.id == id }?.nome }
                ResultadoCalculo.Falha(Problema(listOfNotNull(nome, f.message).joinToString(": "), criterioId = criterioId))
            }
        }
    }
}
