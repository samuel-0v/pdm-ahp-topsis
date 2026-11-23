package br.edu.ufu.pdm.mcdm.domain.engine

import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

sealed interface SaidaTopsis {
    /** C* definido para todas as alternativas, na ordem de entrada (sem ordenar). */
    data class Definido(val dMais: List<Double>, val dMenos: List<Double>, val coeficientes: List<Double>) : SaidaTopsis

    /** Ausência legítima de diferenciação: d+ = d− = 0 para todas; C* indefinido. */
    data class SemDiferenciacao(val dMais: List<Double>, val dMenos: List<Double>) : SaidaTopsis
}

/**
 * TOPSIS com normalização vetorial, ponderação, ideais por natureza, distâncias euclidianas e
 * C* = d− / (d+ + d−) apenas com denominador definido. Não arredonda intermediários.
 * Função pura: recebe matriz Double já preparada (conversão monetária e validações no Domain).
 */
object TopsisEngine {
    fun calcular(matriz: List<List<Double>>, pesos: List<Double>, beneficio: List<Boolean>): SaidaTopsis {
        val m = matriz.size
        val n = pesos.size
        if (m < 2) throw FalhaMatematica("O TOPSIS requer pelo menos duas alternativas.")
        if (n == 0 || matriz.any { it.size != n } || beneficio.size != n) {
            throw FalhaMatematica("Dimensões incompatíveis entre matriz, pesos e naturezas.")
        }
        if (pesos.any { !it.isFinite() || it <= 0.0 }) throw FalhaMatematica("Pesos devem ser positivos e finitos.")
        for (i in 0 until m) for (j in 0 until n) {
            if (!matriz[i][j].isFinite()) throw FalhaMatematica("Valor não finito.", j)
        }

        // Normalização vetorial com escala pelo maior módulo para evitar overflow/underflow em x².
        val normalizada = Array(m) { DoubleArray(n) }
        for (j in 0 until n) {
            val escala = (0 until m).maxOf { kotlin.math.abs(matriz[it][j]) }
            if (escala == 0.0) throw FalhaMatematica("Coluna com norma zero: normalização indefinida.", j)
            val norma = escala * sqrt((0 until m).sumOf { val r = matriz[it][j] / escala; r * r })
            if (!norma.isFinite() || norma <= 0.0) throw FalhaMatematica("Norma numericamente inviável.", j)
            for (i in 0 until m) {
                val r = matriz[i][j] / norma
                if (!r.isFinite()) throw FalhaMatematica("Normalização não finita.", j)
                normalizada[i][j] = r
            }
        }

        val ponderada = Array(m) { i -> DoubleArray(n) { j -> normalizada[i][j] * pesos[j] } }
        val idealPositivo = DoubleArray(n)
        val idealNegativo = DoubleArray(n)
        for (j in 0 until n) {
            var maior = ponderada[0][j]
            var menor = ponderada[0][j]
            for (i in 1 until m) {
                maior = max(maior, ponderada[i][j]); menor = min(menor, ponderada[i][j])
            }
            idealPositivo[j] = if (beneficio[j]) maior else menor
            idealNegativo[j] = if (beneficio[j]) menor else maior
        }

        val dMais = List(m) { i -> sqrt((0 until n).sumOf { j -> val d = ponderada[i][j] - idealPositivo[j]; d * d }) }
        val dMenos = List(m) { i -> sqrt((0 until n).sumOf { j -> val d = ponderada[i][j] - idealNegativo[j]; d * d }) }
        if ((dMais + dMenos).any { !it.isFinite() || it < 0.0 }) throw FalhaMatematica("Distâncias não finitas.")

        val denominadoresZero = (0 until m).count { dMais[it] + dMenos[it] == 0.0 }
        if (denominadoresZero == m) return SaidaTopsis.SemDiferenciacao(dMais, dMenos)
        // d+ = d− = 0 em uma alternativa implica ideais iguais e, portanto, em todas.
        // Um caso parcial só ocorreria por falha numérica: não é classificado como sem diferenciação.
        if (denominadoresZero > 0) throw FalhaMatematica("Distâncias inconsistentes: denominador nulo parcial.")

        val coeficientes = List(m) { i -> dMenos[i] / (dMais[i] + dMenos[i]) }
        if (coeficientes.any { !it.isFinite() || it < 0.0 || it > 1.0 }) throw FalhaMatematica("Coeficiente C* fora de [0, 1].")
        return SaidaTopsis.Definido(dMais, dMenos, coeficientes)
    }
}
