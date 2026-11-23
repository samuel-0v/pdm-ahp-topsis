package br.edu.ufu.pdm.mcdm.domain.validation

import br.edu.ufu.pdm.mcdm.domain.Fixtures
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EntradaPtBrTest {
    private val preco = Fixtures.criteriosSmartphone[1]
    private val camera = Fixtures.criteriosSmartphone[2]
    private val autonomia = Fixtures.criteriosSmartphone[3]

    private fun centavos(t: String) = ((EntradaPtBr.interpretar(t, preco) as ResultadoEntrada.Valido).valor as ValorCanonico.Monetario).centavos

    @Test
    fun `aceita formatos pt-BR sem ambiguidade`() {
        assertEquals(500000L, centavos("5000"))
        assertEquals(500000L, centavos("5.000"))
        assertEquals(500000L, centavos("5.000,00"))
        assertEquals(500050L, centavos("R$ 5.000,50"))
        assertEquals(123L, centavos("1,230"))
        assertEquals(123456789L, centavos("1.234.567,89"))
    }

    @Test
    fun `rejeita fracao de centavo e separadores ambiguos`() {
        assertTrue(EntradaPtBr.interpretar("1,231", preco) is ResultadoEntrada.Invalido)
        assertTrue(EntradaPtBr.interpretar("5.5", preco) is ResultadoEntrada.Invalido)
        assertTrue(EntradaPtBr.interpretar("50.00", preco) is ResultadoEntrada.Invalido)
        assertTrue(EntradaPtBr.interpretar("1,2,3", preco) is ResultadoEntrada.Invalido)
        assertTrue(EntradaPtBr.interpretar("abc", preco) is ResultadoEntrada.Invalido)
    }

    @Test
    fun `detecta overflow de Long`() {
        assertTrue(EntradaPtBr.interpretar("92233720368547758,08", preco) is ResultadoEntrada.Invalido)
        assertEquals(Long.MAX_VALUE, centavos("92233720368547758,07"))
    }

    @Test
    fun `vazio e ausencia, nao zero`() {
        assertEquals(ResultadoEntrada.Vazio, EntradaPtBr.interpretar("  ", preco))
        assertEquals(ResultadoEntrada.Valido(ValorCanonico.Monetario(0)), EntradaPtBr.interpretar("0", preco))
    }

    @Test
    fun `numero usa virgula e respeita limites das notas`() {
        assertEquals(ResultadoEntrada.Valido(ValorCanonico.Numero(24.5)), EntradaPtBr.interpretar("24,5", autonomia))
        assertTrue(EntradaPtBr.interpretar("10", camera) is ResultadoEntrada.Valido)
        assertTrue(EntradaPtBr.interpretar("1", camera) is ResultadoEntrada.Valido)
        assertTrue(EntradaPtBr.interpretar("10,5", camera) is ResultadoEntrada.Invalido)
        assertTrue(EntradaPtBr.interpretar("0", camera) is ResultadoEntrada.Invalido)
    }

    @Test
    fun `formatacao para edicao faz round-trip`() {
        for (v in listOf(ValorCanonico.Monetario(799990), ValorCanonico.Monetario(5), ValorCanonico.Numero(24.5), ValorCanonico.Numero(256.0))) {
            val crit = if (v is ValorCanonico.Monetario) preco else autonomia
            assertEquals(ResultadoEntrada.Valido(v), EntradaPtBr.interpretar(Formatos.paraEdicao(v), crit))
        }
    }

    @Test
    fun `RC proxima do limite nao aparece como aceita`() {
        val texto = Formatos.rc(0.1000000004)
        assertTrue(texto, texto.replace(',', '.').toDouble() > 0.10)
        assertEquals("0,0500", Formatos.rc(0.05))
    }
}

class CompatibilidadeTest {
    private val c = Fixtures.criteriosSmartphone

    @Test
    fun `mudanca editorial e compativel`() {
        val editorial = c.map { it.copy(nome = it.nome + " (novo nome)", ordem = 10 - it.ordem) }
        assertEquals(Compatibilidade.Compativel, CompatibilidadeDefinicoes.comparar(editorial, c))
    }

    @Test
    fun `unidade ou natureza diferente e incompativel`() {
        val outra = c.map { if (it.id == "smartphones-autonomia") it.copy(unidade = "MINUTOS") else it }
        assertTrue(CompatibilidadeDefinicoes.comparar(outra, c) is Compatibilidade.Incompativel)
        val natureza = c.map { if (it.id == "smartphones-preco") it.copy(natureza = br.edu.ufu.pdm.mcdm.domain.model.Natureza.BENEFICIO) else it }
        assertTrue(CompatibilidadeDefinicoes.comparar(natureza, c) is Compatibilidade.Incompativel)
    }

    @Test
    fun `sem definicoes e indeterminado`() {
        assertTrue(CompatibilidadeDefinicoes.comparar(emptyList(), c) is Compatibilidade.Indeterminada)
    }
}
