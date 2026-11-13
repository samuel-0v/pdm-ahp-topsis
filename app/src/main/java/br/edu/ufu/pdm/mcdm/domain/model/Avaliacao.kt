package br.edu.ufu.pdm.mcdm.domain.model

import java.time.Instant

/** Situação da última versão persistida (Parte 4). */
enum class EstadoSalvo { RASCUNHO, CONCLUIDA_RANKING, CONCLUIDA_SEM_DIFERENCIACAO }

/** Direção canônica do julgamento sobre o par (menor, maior) normalizado. */
enum class DirecaoJulgamento { IGUAL, MENOR_PREFERIDO, MAIOR_PREFERIDO }

/** Par de critérios normalizado pela ordem binária UTF-8 dos IDs (mesma do BINARY do SQLite). */
data class ParCriterios private constructor(val menorId: String, val maiorId: String) {
    companion object {
        fun de(a: String, b: String): ParCriterios {
            require(a != b) { "Par AHP exige dois critérios distintos." }
            return if (compararIds(a, b) < 0) ParCriterios(a, b) else ParCriterios(b, a)
        }

        /** Construção direta usada ao carregar; rejeita par fora da forma canônica. */
        fun canonico(menorId: String, maiorId: String): ParCriterios {
            require(compararIds(menorId, maiorId) < 0) { "Par AHP fora da ordem canônica: $menorId/$maiorId." }
            return ParCriterios(menorId, maiorId)
        }

        fun compararIds(a: String, b: String): Int {
            val x = a.toByteArray(Charsets.UTF_8)
            val y = b.toByteArray(Charsets.UTF_8)
            for (i in 0 until minOf(x.size, y.size)) {
                val d = (x[i].toInt() and 0xFF) - (y[i].toInt() and 0xFF)
                if (d != 0) return d
            }
            return x.size - y.size
        }
    }
}

/** Julgamento AHP canônico: intensidade inteira 1–9; intensidade 1 usa direção IGUAL. */
data class Julgamento(val par: ParCriterios, val intensidade: Int, val direcao: DirecaoJulgamento) {
    init {
        require(intensidade in 1..9) { "Intensidade AHP deve estar entre 1 e 9." }
        require((intensidade == 1) == (direcao == DirecaoJulgamento.IGUAL)) {
            "Intensidade 1 corresponde exatamente à direção IGUAL."
        }
    }

    /** Valor de a[menor][maior] na matriz recíproca. */
    val valorMenorSobreMaior: Double
        get() = when (direcao) {
            DirecaoJulgamento.IGUAL -> 1.0
            DirecaoJulgamento.MENOR_PREFERIDO -> intensidade.toDouble()
            DirecaoJulgamento.MAIOR_PREFERIDO -> 1.0 / intensidade
        }

    /** Critério preferido, ou null se igual. */
    val preferidoId: String?
        get() = when (direcao) {
            DirecaoJulgamento.IGUAL -> null
            DirecaoJulgamento.MENOR_PREFERIDO -> par.menorId
            DirecaoJulgamento.MAIOR_PREFERIDO -> par.maiorId
        }

    companion object {
        /** Converte a resposta visual (critério preferido ou null para igual) para a forma canônica. */
        fun deResposta(a: String, b: String, preferidoId: String?, intensidade: Int): Julgamento {
            val par = ParCriterios.de(a, b)
            if (preferidoId == null || intensidade == 1) return Julgamento(par, 1, DirecaoJulgamento.IGUAL)
            require(preferidoId == a || preferidoId == b) { "Critério preferido fora do par." }
            val direcao = if (preferidoId == par.menorId) DirecaoJulgamento.MENOR_PREFERIDO else DirecaoJulgamento.MAIOR_PREFERIDO
            return Julgamento(par, intensidade, direcao)
        }
    }
}

/** Valor de uma alternativa: original e efetivo independentes, cada um com proveniência própria. */
data class ValorAlternativa(
    val origem: ValorCanonico?,
    val provenienciaOrigem: Proveniencia,
    val efetivo: ValorCanonico?,
    val provenienciaEfetivo: Proveniencia,
    /** Texto ainda inválido preservado em rascunho; não participa de cálculo. */
    val entradaPendente: String? = null,
) {
    val ajustado: Boolean get() = efetivo != origem || provenienciaEfetivo.ajustadoNaAvaliacao

    companion object {
        fun copiaDoOriginal(valor: ValorCanonico?, proveniencia: Proveniencia) =
            ValorAlternativa(valor, proveniencia, valor, proveniencia)
    }
}

data class Alternativa(
    /** UUID contextual da participação; estável nas edições da mesma avaliação. */
    val alternativaId: String,
    val origemProduto: OrigemProduto,
    val idProdutoOrigem: String,
    val identificacao: IdentificacaoProduto,
    val ordem: Int,
    val valores: Map<String, ValorAlternativa>,
    /** Proveniência da consulta: data de atualização do publicador quando conhecida. */
    val atualizadoNaOrigemEm: Instant? = null,
) {
    val chaveOrigem: String get() = "${origemProduto.name}:$idProdutoOrigem"
}

data class ResultadoAhp(
    val pesos: Map<String, Double>,
    val lambdaMax: Double,
    val ic: Double,
    val ri: Double,
    /** null quando RI = 0 (n ≤ 2): consistência não definida. */
    val rc: Double?,
) {
    /** Comparação interna direta com 0,10, sem arredondamento (DEC-018). */
    val consistente: Boolean get() = rc == null || rc <= LIMITE_RC

    companion object {
        const val LIMITE_RC = 0.10
    }
}

data class DetalheRanking(val dMais: Double, val dMenos: Double, val coeficiente: Double, val posicao: Int)
data class Distancias(val dMais: Double, val dMenos: Double)

sealed interface ResultadoTopsis {
    data class Ranking(val detalhes: Map<String, DetalheRanking>) : ResultadoTopsis {
        /** Tamanho do grupo de empate de cada posição. */
        fun tamanhoGrupo(posicao: Int): Int = detalhes.values.count { it.posicao == posicao }
    }

    /** d+ = d− = 0 para todas: C* e posição ausentes, sem vencedor (DEC-024/035). */
    data class SemDiferenciacao(val distancias: Map<String, Distancias>) : ResultadoTopsis
}

/**
 * Raiz do agregado (DEC-036). Representa tanto a versão salva quanto o conteúdo salvável de uma edição.
 * Julgamentos são indexados pelo par canônico; pares pendentes não aparecem.
 */
data class Avaliacao(
    val id: String,
    val categoriaId: String,
    val categoriaNome: String,
    val revisaoDefinicao: String?,
    val titulo: String?,
    val criterios: List<CriterioDefinicao>,
    val alternativas: List<Alternativa>,
    val julgamentos: Map<ParCriterios, Julgamento>,
    val ahp: ResultadoAhp?,
    val topsis: ResultadoTopsis?,
    val criadaEm: Instant,
    val salvaEm: Instant?,
) {
    val criteriosOrdenados: List<CriterioDefinicao> get() = criterios.sortedBy { it.ordem }
    val alternativasOrdenadas: List<Alternativa> get() = alternativas.sortedBy { it.ordem }

    val estado: EstadoSalvo
        get() = when (topsis) {
            null -> EstadoSalvo.RASCUNHO
            is ResultadoTopsis.Ranking -> EstadoSalvo.CONCLUIDA_RANKING
            is ResultadoTopsis.SemDiferenciacao -> EstadoSalvo.CONCLUIDA_SEM_DIFERENCIACAO
        }

    /** Todos os pares da categoria na ordem visual proposta (ordem dos critérios). */
    val paresEsperados: List<Pair<CriterioDefinicao, CriterioDefinicao>>
        get() {
            val c = criteriosOrdenados
            return buildList { for (i in c.indices) for (j in i + 1 until c.size) add(c[i] to c[j]) }
        }

    val julgamentosCompletos: Boolean get() = julgamentos.size == paresEsperados.size

    val rotulo: String get() = titulo?.takeIf { it.isNotBlank() } ?: categoriaNome
}

/** Resumo para a lista T05: reflete apenas a versão efetivamente salva. */
data class ResumoAvaliacao(
    val id: String,
    val rotulo: String,
    val categoriaNome: String,
    val estado: EstadoSalvo,
    val quantidadeAlternativas: Int,
    val salvaEm: Instant,
)
