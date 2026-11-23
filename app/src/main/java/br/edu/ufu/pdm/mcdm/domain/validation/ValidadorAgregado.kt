package br.edu.ufu.pdm.mcdm.domain.validation

import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.ResultadoTopsis
import br.edu.ufu.pdm.mcdm.domain.engine.PoliticaRanking

/**
 * Invariantes do agregado validadas ao salvar E ao carregar (DEC-039, contrato 02).
 * Não executa o motor: confere completude, faixas e correspondência dos componentes.
 */
object ValidadorAgregado {
    fun problemas(a: Avaliacao): List<String> {
        val p = mutableListOf<String>()
        val ids = a.criterios.map { it.id }
        if (ids.toSet().size != ids.size) p += "Critérios repetidos."
        if (a.criterios.map { it.ordem }.toSet().size != a.criterios.size) p += "Ordem de critérios repetida."
        if (a.alternativas.map { it.chaveOrigem }.toSet().size != a.alternativas.size) p += "Alternativa repetida (origem + ID)."
        if (a.alternativas.map { it.alternativaId }.toSet().size != a.alternativas.size) p += "ID contextual de alternativa repetido."
        for (alt in a.alternativas) {
            val extras = alt.valores.keys - ids.toSet()
            if (extras.isNotEmpty()) p += "Valor de critério externo em ${alt.identificacao.nome}."
        }
        for (j in a.julgamentos.values) {
            if (j.par.menorId !in ids || j.par.maiorId !in ids) p += "Julgamento referencia critério externo."
        }

        a.ahp?.let { ahp ->
            if (!a.julgamentosCompletos) p += "Resultado AHP com julgamentos incompletos."
            if (ahp.pesos.keys != ids.toSet()) p += "Pesos AHP não correspondem exatamente aos critérios."
            if (ahp.pesos.values.any { !it.isFinite() || it <= 0.0 }) p += "Peso AHP inválido."
            if (kotlin.math.abs(ahp.pesos.values.sum() - 1.0) > 1e-9) p += "Pesos AHP não normalizados."
            if (!ahp.lambdaMax.isFinite() || !ahp.ic.isFinite() || !ahp.ri.isFinite() || ahp.rc?.isFinite() == false) {
                p += "Indicadores AHP não finitos."
            }
        }

        when (val t = a.topsis) {
            null -> Unit
            else -> {
                if (a.ahp?.consistente != true) p += "TOPSIS exige AHP completo e consistente."
                if (a.alternativas.size < 2) p += "TOPSIS exige pelo menos duas alternativas."
                if (a.alternativas.any { alt -> a.criterios.any { alt.valores[it.id]?.efetivo == null || alt.valores[it.id]?.entradaPendente != null } }) {
                    p += "Conclusão com valor efetivo ausente ou pendente."
                }
                val altIds = a.alternativas.map { it.alternativaId }.toSet()
                when (t) {
                    is ResultadoTopsis.Ranking -> {
                        if (t.detalhes.keys != altIds) p += "Detalhe TOPSIS faltante ou extra."
                        val d = t.detalhes.values
                        if (d.any { !it.dMais.isFinite() || !it.dMenos.isFinite() || it.dMais < 0 || it.dMenos < 0 }) p += "Distância TOPSIS inválida."
                        if (d.any { !it.coeficiente.isFinite() || it.coeficiente !in 0.0..1.0 }) p += "Coeficiente C* inválido."
                        val ordemIds = t.detalhes.keys.toList()
                        val esperadas = PoliticaRanking.posicoes(ordemIds.map { t.detalhes.getValue(it).coeficiente })
                        if (ordemIds.indices.any { t.detalhes.getValue(ordemIds[it]).posicao != esperadas[it] }) p += "Posições não correspondem aos coeficientes."
                    }
                    is ResultadoTopsis.SemDiferenciacao -> {
                        if (t.distancias.keys != altIds) p += "Distâncias faltantes ou extras."
                        if (t.distancias.values.any { it.dMais != 0.0 || it.dMenos != 0.0 }) p += "Sem diferenciação exige d+ = d− = 0."
                    }
                }
            }
        }
        return p
    }
}
