package br.edu.ufu.pdm.mcdm.domain.exportacao

import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.domain.model.ResultadoTopsis
import br.edu.ufu.pdm.mcdm.domain.model.Unidades
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import br.edu.ufu.pdm.mcdm.domain.validation.Formatos
import java.time.Instant

/** Conteúdo aprovado na DEC-032, já formatado; o adaptador Android apenas desenha. */
data class ConteudoRelatorio(
    val titulo: String,
    val categoria: String,
    val geradoEm: Instant,
    val salvaEm: Instant?,
    val criterios: List<LinhaCriterio>,
    val indicadoresAhp: List<Pair<String, String>>,
    val situacao: String,
    val alternativas: List<LinhaAlternativa>,
    val metodo: String,
    /** Nome da alternativa vencedora única; null em empate na 1ª posição ou sem diferenciação. */
    val vencedora: String? = null,
) {
    data class LinhaCriterio(val nome: String, val unidade: String, val natureza: String, val peso: String, val pesoValor: Double = 0.0)
    data class LinhaAlternativa(
        val nome: String,
        val origem: String,
        val posicao: String,
        val coeficiente: String,
        val dMais: String,
        val dMenos: String,
        val valores: List<ValorRelatorio>,
        val coeficienteValor: Double? = null,
    )
    data class ValorRelatorio(val criterio: String, val texto: String, val ajustado: Boolean)

    companion object {
        const val METODO =
            "Pesos dos critérios pelo AHP (média geométrica normalizada; λmax, IC = (λmax − n)/(n − 1), RC = IC/RI, limite 0,10) " +
                "e ordenação pelo TOPSIS (normalização vetorial, ponderação, ideais positivo/negativo, distâncias euclidianas, " +
                "C* = d−/(d+ + d−)). Empates: tolerância 10⁻⁹, posições competitivas. Referência: ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf."

        fun de(a: Avaliacao, geradoEm: Instant): ConteudoRelatorio {
            val ahp = requireNotNull(a.ahp)
            val topsis = requireNotNull(a.topsis)
            val crit = a.criteriosOrdenados
            val situacao = when (topsis) {
                is ResultadoTopsis.Ranking -> {
                    val primeiros = topsis.detalhes.values.count { it.posicao == 1 }
                    val empates = topsis.detalhes.values.groupBy { it.posicao }.filterValues { it.size > 1 }
                    buildString {
                        append("Concluída com ranking.")
                        if (primeiros > 1) append(" Empate na primeira posição entre $primeiros alternativas; sem vencedor único.")
                        if (empates.isNotEmpty()) append(" Empates: " + empates.keys.sorted().joinToString { "${it}º (${empates.getValue(it).size})" } + ".")
                    }
                }
                is ResultadoTopsis.SemDiferenciacao ->
                    "Concluída — sem diferenciação entre as alternativas. d+ = d− = 0; C*, posição e vencedor não definidos."
            }
            val alternativas = a.alternativasOrdenadas.map { alt ->
                // Unidade no cabeçalho da coluna; a célula leva só o número.
                val valores = crit.map { c ->
                    val v = alt.valores[c.id]
                    val texto = when (val e = v?.efetivo) {
                        null -> "não informado"
                        is ValorCanonico.Monetario -> Formatos.centavos(e.centavos).removePrefix("R$ ")
                        is ValorCanonico.Numero -> Formatos.numero(e.valor)
                    }
                    ValorRelatorio("${c.nome} (${Unidades.rotulo(c.unidade)})", texto, v?.ajustado == true)
                }
                val origem = if (alt.origemProduto == OrigemProduto.OFICIAL) "Oficial" else "Pessoal"
                when (topsis) {
                    is ResultadoTopsis.Ranking -> {
                        val d = topsis.detalhes.getValue(alt.alternativaId)
                        val empate = topsis.tamanhoGrupo(d.posicao).let { if (it > 1) " (empate)" else "" }
                        LinhaAlternativa(alt.identificacao.nome, origem, "${d.posicao}º$empate", Formatos.fixo(d.coeficiente, 6), Formatos.fixo(d.dMais, 6), Formatos.fixo(d.dMenos, 6), valores, d.coeficiente)
                    }
                    is ResultadoTopsis.SemDiferenciacao -> {
                        val d = topsis.distancias.getValue(alt.alternativaId)
                        LinhaAlternativa(alt.identificacao.nome, origem, "não definida", "não definido", Formatos.fixo(d.dMais, 6), Formatos.fixo(d.dMenos, 6), valores)
                    }
                }
            }.let { lista ->
                if (topsis is ResultadoTopsis.Ranking) lista.sortedBy { it.posicao.takeWhile(Char::isDigit).toInt() } else lista
            }
            return ConteudoRelatorio(
                titulo = a.rotulo,
                categoria = a.categoriaNome,
                geradoEm = geradoEm,
                salvaEm = a.salvaEm,
                criterios = crit.map {
                    LinhaCriterio(it.nome, Unidades.rotulo(it.unidade), if (it.natureza == Natureza.BENEFICIO) "Benefício" else "Custo", Formatos.percentual(ahp.pesos.getValue(it.id)), ahp.pesos.getValue(it.id))
                },
                indicadoresAhp = listOf(
                    "λmax" to Formatos.fixo(ahp.lambdaMax, 6),
                    "IC" to Formatos.fixo(ahp.ic, 6),
                    "RI" to Formatos.fixo(ahp.ri, 2),
                    "RC" to (ahp.rc?.let { Formatos.rc(it) } ?: "não definida (n ≤ 2)"),
                ),
                situacao = situacao,
                alternativas = alternativas,
                metodo = METODO,
                vencedora = (topsis as? ResultadoTopsis.Ranking)?.takeIf { it.tamanhoGrupo(1) == 1 }?.let { r ->
                    val id = r.detalhes.entries.first { it.value.posicao == 1 }.key
                    a.alternativas.first { it.alternativaId == id }.identificacao.nome
                },
            )
        }
    }
}

data class RelatorioGerado(val caminhoArquivo: String, val nomeArquivo: String)

/** Porta do Domain; a implementação Android usa PdfDocument e arquivo privado (DEC-048). */
interface GeradorRelatorioPdf {
    suspend fun gerar(conteudo: ConteudoRelatorio): RelatorioGerado
}
