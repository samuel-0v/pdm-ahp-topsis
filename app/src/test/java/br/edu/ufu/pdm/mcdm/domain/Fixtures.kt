package br.edu.ufu.pdm.mcdm.domain

import br.edu.ufu.pdm.mcdm.domain.model.Alternativa
import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.IdentificacaoProduto
import br.edu.ufu.pdm.mcdm.domain.model.Julgamento
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.domain.model.Proveniencia
import br.edu.ufu.pdm.mcdm.domain.model.TipoValor
import br.edu.ufu.pdm.mcdm.domain.model.Validacao
import br.edu.ufu.pdm.mcdm.domain.model.ValorAlternativa
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import java.time.Instant

object Fixtures {
    private fun num(min: Double? = null, max: Double? = null) = Validacao(
        TipoValor.NUMERO, true, true, false,
        minimo = min?.let { ValorCanonico.Numero(it) }, maximo = max?.let { ValorCanonico.Numero(it) },
    )

    private val monetario = Validacao(TipoValor.MONETARIO_BRL, true, true, false)

    val criteriosSmartphone = listOf(
        CriterioDefinicao("smartphones-geekbench", "Geekbench 6 Multi-Core", 0, "PONTOS", Natureza.BENEFICIO, num()),
        CriterioDefinicao("smartphones-preco", "Preço", 1, "BRL", Natureza.CUSTO, monetario),
        CriterioDefinicao("smartphones-cameras", "Câmeras", 2, "NOTA_1_10", Natureza.BENEFICIO, num(1.0, 10.0)),
        CriterioDefinicao("smartphones-autonomia", "Autonomia", 3, "HORAS", Natureza.BENEFICIO, num()),
        CriterioDefinicao("smartphones-armazenamento", "Armazenamento", 4, "GB", Natureza.BENEFICIO, num()),
    )

    fun alternativa(id: String, valores: List<Number>, origem: OrigemProduto = OrigemProduto.OFICIAL, ordem: Int = 0) = Alternativa(
        alternativaId = "alt-$id",
        origemProduto = origem,
        idProdutoOrigem = id,
        identificacao = IdentificacaoProduto(id),
        ordem = ordem,
        valores = criteriosSmartphone.zip(valores).associate { (c, v) ->
            val canon: ValorCanonico = if (c.validacao.tipoValor == TipoValor.MONETARIO_BRL) {
                ValorCanonico.Monetario(v.toLong() * 100)
            } else {
                ValorCanonico.Numero(v.toDouble())
            }
            c.id to ValorAlternativa.copiaDoOriginal(canon, Proveniencia.DESCONHECIDA)
        },
    )

    fun julgamentosIguais(): Map<br.edu.ufu.pdm.mcdm.domain.model.ParCriterios, Julgamento> {
        val c = criteriosSmartphone
        return buildMap {
            for (i in c.indices) for (j in i + 1 until c.size) {
                val jg = Julgamento.deResposta(c[i].id, c[j].id, null, 1)
                put(jg.par, jg)
            }
        }
    }

    fun avaliacao(alternativas: List<Alternativa>) = Avaliacao(
        id = "00000000-0000-4000-8000-000000000001",
        categoriaId = "smartphones-premium",
        categoriaNome = "Smartphones Premium",
        revisaoDefinicao = "r1",
        titulo = null,
        criterios = criteriosSmartphone,
        alternativas = alternativas,
        julgamentos = emptyMap(),
        ahp = null,
        topsis = null,
        criadaEm = Instant.parse("2026-11-13T10:00:00Z"),
        salvaEm = null,
    )
}
