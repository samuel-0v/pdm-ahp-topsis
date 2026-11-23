package br.edu.ufu.pdm.mcdm.domain.validation

import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao

sealed interface Compatibilidade {
    data object Compativel : Compatibilidade
    data class Incompativel(val motivos: List<String>) : Compatibilidade
    data class Indeterminada(val motivos: List<String>) : Compatibilidade
}

/**
 * Comparação de definições por ID e significado (DEC-042, contrato 05).
 * Nome, descrição e ordem são editoriais. Unidade, natureza, tipo, moeda/escala mudam a
 * comparabilidade. Limites diferentes exigem apenas revalidar cada valor no destino.
 */
object CompatibilidadeDefinicoes {
    fun comparar(origem: List<CriterioDefinicao>, destino: List<CriterioDefinicao>): Compatibilidade {
        if (origem.isEmpty()) return Compatibilidade.Indeterminada(listOf("Definições de origem indisponíveis."))
        val porId = origem.associateBy { it.id }
        val motivos = mutableListOf<String>()
        for (d in destino) {
            val o = porId[d.id]
            if (o == null) {
                motivos += "Critério ${d.nome} não existe nas definições do produto."
                continue
            }
            if (o.unidade != d.unidade) motivos += "${d.nome}: unidade ${o.unidade} ≠ ${d.unidade}."
            if (o.natureza != d.natureza) motivos += "${d.nome}: natureza diferente."
            if (o.validacao.tipoValor != d.validacao.tipoValor) motivos += "${d.nome}: tipo de valor diferente."
        }
        val extras = origem.map { it.id }.toSet() - destino.map { it.id }.toSet()
        if (extras.isNotEmpty()) motivos += "Critérios fora do destino: ${extras.joinToString()}."
        return if (motivos.isEmpty()) Compatibilidade.Compativel else Compatibilidade.Incompativel(motivos)
    }
}
