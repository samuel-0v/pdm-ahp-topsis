package br.edu.ufu.pdm.mcdm.data

import br.edu.ufu.pdm.mcdm.data.remote.DocumentoInvalidoException
import br.edu.ufu.pdm.mcdm.data.remote.FirestoreMapper
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.TipoValor
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import br.edu.ufu.pdm.mcdm.domain.model.ValorLido
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Mapas no formato que o SDK entrega (integerValue → Long, doubleValue → Double). */
class FirestoreMapperTest {
    private fun criterio(id: String, ordem: Long, tipo: String, natureza: String = "BENEFICIO") = mapOf(
        "id" to id, "nome" to id, "ordem" to ordem, "unidade" to "U", "natureza" to natureza,
        "validacao" to buildMap {
            put("tipoValor", tipo); put("obrigatorioCadastro", true); put("obrigatorioProcessamento", true); put("exigeInteiro", false)
            if (tipo == "MONETARIO_BRL") { put("moeda", "BRL"); put("escala", 2L) }
            if (id == "nota") put("limites", mapOf("minimo" to 1.0, "minimoInclusivo" to true, "maximo" to 10.0, "maximoInclusivo" to true))
        },
    )

    private val categoriaDoc = mapOf(
        "formatoContrato" to 1L, "nome" to "Cat", "revisaoDefinicao" to "r1",
        "criterios" to listOf(criterio("preco", 1, "MONETARIO_BRL", "CUSTO"), criterio("nota", 0, "NUMERO")),
    )

    @Test
    fun `categoria valida ordena criterios e le limites`() {
        val c = FirestoreMapper.categoria("cat", categoriaDoc)
        assertEquals(listOf("nota", "preco"), c.criterios.map { it.id })
        assertEquals(Natureza.CUSTO, c.criterios[1].natureza)
        assertEquals(ValorCanonico.Numero(10.0), c.criterios[0].validacao.maximo)
    }

    @Test(expected = DocumentoInvalidoException::class)
    fun `formato de contrato desconhecido e rejeitado`() {
        FirestoreMapper.categoria("cat", categoriaDoc + ("formatoContrato" to 2L))
    }

    @Test(expected = DocumentoInvalidoException::class)
    fun `ordem repetida e rejeitada`() {
        FirestoreMapper.categoria("cat", categoriaDoc + ("criterios" to listOf(criterio("a", 0, "NUMERO"), criterio("b", 0, "NUMERO"))))
    }

    @Test
    fun `envelopes tipados`() {
        assertEquals(ValorLido.Valido(ValorCanonico.Monetario(12345)), FirestoreMapper.valor(mapOf("tipo" to "MONETARIO_BRL", "centavos" to 12345L, "moeda" to "BRL", "escala" to 2L), TipoValor.MONETARIO_BRL))
        assertTrue(FirestoreMapper.valor(mapOf("tipo" to "MONETARIO_BRL", "centavos" to 123.45, "moeda" to "BRL", "escala" to 2L), TipoValor.MONETARIO_BRL) is ValorLido.Incompativel)
        assertTrue(FirestoreMapper.valor(5000.0, TipoValor.MONETARIO_BRL) is ValorLido.Incompativel)
        assertEquals(ValorLido.Ausente, FirestoreMapper.valor(null, TipoValor.NUMERO))
        assertEquals(ValorLido.Valido(ValorCanonico.Numero(16.0)), FirestoreMapper.valor(mapOf("tipo" to "NUMERO", "numero" to 16L), TipoValor.NUMERO))
        assertTrue(FirestoreMapper.valor(mapOf("tipo" to "NUMERO", "numero" to 1.0), TipoValor.MONETARIO_BRL) is ValorLido.Incompativel)
    }

    @Test
    fun `produto com criterio desconhecido registra problema e ausencia nao vira zero`() {
        val cats = mapOf("cat" to FirestoreMapper.categoria("cat", categoriaDoc))
        val p = FirestoreMapper.produto(
            "p1",
            mapOf(
                "formatoContrato" to 1L, "categoriaId" to "cat", "revisaoDefinicao" to "r1", "nome" to "P",
                "valores" to mapOf("preco" to mapOf("tipo" to "MONETARIO_BRL", "centavos" to 100L, "moeda" to "BRL", "escala" to 2L), "outro" to null),
            ),
            cats,
        )
        assertEquals(ValorLido.Ausente, p.valores["nota"])
        assertEquals(1, p.problemas.size)
    }
}
