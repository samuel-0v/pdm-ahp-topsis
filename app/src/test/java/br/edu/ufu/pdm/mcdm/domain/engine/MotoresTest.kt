package br.edu.ufu.pdm.mcdm.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import kotlin.math.abs

/** Tolerância combinada da DEC-029: |calc − ref| ≤ 10⁻⁹ + 10⁻⁹·|ref|. */
fun assertProximo(esperado: Double, obtido: Double, contexto: String = "") {
    assertTrue("$contexto esperado=$esperado obtido=$obtido", abs(obtido - esperado) <= 1e-9 + 1e-9 * abs(esperado))
}

class AhpEngineTest {
    private fun matrizDePesos(w: List<Double>) = List(w.size) { i -> List(w.size) { j -> w[i] / w[j] } }

    @Test
    fun `matriz perfeitamente consistente recupera pesos conhecidos`() {
        val w = listOf(0.4, 0.25, 0.15, 0.12, 0.08)
        val s = AhpEngine.calcular(matrizDePesos(w))
        w.forEachIndexed { i, e -> assertProximo(e, s.pesos[i], "peso $i") }
        assertProximo(5.0, s.lambdaMax)
        assertTrue(abs(s.ic) <= 1e-12)
        assertEquals(1.12, s.ri, 0.0)
        assertTrue(abs(s.rc!!) <= 1e-12)
    }

    @Test
    fun `igualdade nos dez pares produz pesos de 20 por cento`() {
        val s = AhpEngine.calcular(List(5) { List(5) { 1.0 } })
        s.pesos.forEach { assertProximo(0.2, it) }
        assertProximo(5.0, s.lambdaMax)
    }

    @Test
    fun `ciclo intransitivo de intensidade 9 e inconsistente`() {
        // a > b (9), b > c (9), c > a (9)
        val m = listOf(
            listOf(1.0, 9.0, 1.0 / 9),
            listOf(1.0 / 9, 1.0, 9.0),
            listOf(9.0, 1.0 / 9, 1.0),
        )
        val s = AhpEngine.calcular(m)
        assertTrue("RC=${s.rc}", s.rc!! > 0.10)
        s.pesos.forEach { assertProximo(1.0 / 3, it) }
    }

    @Test
    fun `n igual a 2 tem RC nao definida`() {
        val s = AhpEngine.calcular(listOf(listOf(1.0, 3.0), listOf(1.0 / 3, 1.0)))
        assertNull(s.rc)
        assertProximo(0.75, s.pesos[0])
    }

    @Test
    fun `rejeita matriz nao quadrada, nao positiva ou nao reciproca`() {
        falha { AhpEngine.calcular(listOf(listOf(1.0, 2.0))) }
        falha { AhpEngine.calcular(listOf(listOf(1.0, -2.0), listOf(-0.5, 1.0))) }
        falha { AhpEngine.calcular(listOf(listOf(1.0, 2.0), listOf(2.0, 1.0))) }
        falha { AhpEngine.calcular(listOf(listOf(2.0, 1.0), listOf(1.0, 1.0))) }
        falha { AhpEngine.calcular(List(11) { List(11) { 1.0 } }) }
    }
}

class TopsisEngineTest {
    private val pesos = List(5) { 0.2 }
    private val beneficio = listOf(true, false, true, true, true)

    // Referências do demonstrador HTML (numeros.js), implementação independente — dados-demonstracao.json.
    @Test
    fun `cenario principal confere com referencia independente`() {
        val s = TopsisEngine.calcular(
            listOf(listOf(8000.0, 5000.0, 8.0, 16.0, 256.0), listOf(7000.0, 6000.0, 9.0, 20.0, 512.0)),
            pesos, beneficio,
        ) as SaidaTopsis.Definido
        assertProximo(0.09618457165815669, s.dMais[0]); assertProximo(0.0317760917326689, s.dMenos[0])
        assertProximo(0.2483270318442812, s.coeficientes[0])
        assertProximo(0.0317760917326689, s.dMais[1]); assertProximo(0.09618457165815669, s.dMenos[1])
        assertProximo(0.7516729681557187, s.coeficientes[1])
    }

    @Test
    fun `cenario de empate gera posicoes competitivas 1 2 2 4`() {
        val s = TopsisEngine.calcular(
            listOf(
                listOf(9000.0, 4000.0, 10.0, 24.0, 1024.0),
                listOf(8000.0, 5000.0, 9.0, 20.0, 512.0),
                listOf(8000.0, 5000.0, 9.0, 20.0, 512.0),
                listOf(7000.0, 6000.0, 8.0, 16.0, 256.0),
            ),
            pesos, beneficio,
        ) as SaidaTopsis.Definido
        assertProximo(1.0, s.coeficientes[0])
        assertProximo(0.37393631190004323, s.coeficientes[1])
        assertProximo(0.13655843982241725, s.dMenos[0])
        assertProximo(0.0, s.coeficientes[3])
        assertEquals(listOf(1, 2, 2, 4), PoliticaRanking.posicoes(s.coeficientes))
    }

    @Test
    fun `alternativas identicas resultam em sem diferenciacao, nao em C zero`() {
        val linha = listOf(8000.0, 5000.0, 8.0, 16.0, 256.0)
        val s = TopsisEngine.calcular(listOf(linha, linha), pesos, beneficio)
        assertTrue(s is SaidaTopsis.SemDiferenciacao)
        s as SaidaTopsis.SemDiferenciacao
        assertEquals(listOf(0.0, 0.0), s.dMais)
        assertEquals(listOf(0.0, 0.0), s.dMenos)
    }

    @Test
    fun `coluna com norma zero e falha, nao sem diferenciacao`() {
        try {
            TopsisEngine.calcular(
                listOf(listOf(0.0, 5000.0, 8.0, 16.0, 256.0), listOf(0.0, 6000.0, 9.0, 20.0, 512.0)),
                pesos, beneficio,
            )
            fail("esperava falha")
        } catch (f: FalhaMatematica) {
            assertEquals(0, f.indiceCriterio)
        }
    }

    @Test
    fun `coluna constante positiva e normalizavel`() {
        val s = TopsisEngine.calcular(
            listOf(listOf(5.0, 10.0), listOf(5.0, 20.0)),
            listOf(0.5, 0.5), listOf(true, true),
        ) as SaidaTopsis.Definido
        assertProximo(0.0, s.coeficientes[0]); assertProximo(1.0, s.coeficientes[1])
    }

    @Test
    fun `custo inverte o ideal`() {
        val s = TopsisEngine.calcular(listOf(listOf(100.0), listOf(200.0)), listOf(1.0), listOf(false)) as SaidaTopsis.Definido
        assertProximo(1.0, s.coeficientes[0]); assertProximo(0.0, s.coeficientes[1])
    }

    @Test
    fun `valores enormes nao transbordam na norma`() {
        val s = TopsisEngine.calcular(listOf(listOf(1e300), listOf(2e300)), listOf(1.0), listOf(true)) as SaidaTopsis.Definido
        assertProximo(0.0, s.coeficientes[0]); assertProximo(1.0, s.coeficientes[1])
    }

    @Test
    fun `rejeita dimensoes incompativeis e valores nao finitos`() {
        falha { TopsisEngine.calcular(listOf(listOf(1.0)), listOf(1.0), listOf(true)) }
        falha { TopsisEngine.calcular(listOf(listOf(1.0, 2.0), listOf(1.0)), listOf(0.5, 0.5), listOf(true, true)) }
        falha { TopsisEngine.calcular(listOf(listOf(Double.NaN), listOf(1.0)), listOf(1.0), listOf(true)) }
        falha { TopsisEngine.calcular(listOf(listOf(1.0), listOf(2.0)), listOf(0.0), listOf(true)) }
    }
}

class PoliticaRankingTest {
    @Test
    fun `nao encadeia vizinhos dentro da tolerancia`() {
        // 0.5 e 0.5−0.8e-9 empatam; 0.5−1.6e-9 dista 1.6e-9 do maior do grupo: novo grupo.
        val c = listOf(0.5, 0.5 - 0.8e-9, 0.5 - 1.6e-9, 0.1)
        assertEquals(listOf(1, 1, 3, 4), PoliticaRanking.posicoes(c))
    }

    @Test
    fun `fronteira exata de 1e-9 empata`() {
        assertEquals(listOf(1, 1), PoliticaRanking.posicoes(listOf(0.75, 0.75 - 1e-9 + 1e-17)))
    }

    @Test
    fun `ordem de entrada nao vira desempate`() {
        assertEquals(listOf(2, 1, 2), PoliticaRanking.posicoes(listOf(0.3, 0.9, 0.3)))
    }
}

inline fun falha(bloco: () -> Unit) {
    try {
        bloco(); fail("esperava FalhaMatematica")
    } catch (_: FalhaMatematica) {
    }
}
