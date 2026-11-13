package br.edu.ufu.pdm.mcdm.domain.engine

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.ln

/** Falha de cálculo: não é diagnóstico de inconsistência nem resultado parcial. */
class FalhaMatematica(mensagem: String, val indiceCriterio: Int? = null) : Exception(mensagem)

data class SaidaAhp(
    val pesos: List<Double>,
    val lambdaMax: Double,
    val ic: Double,
    val ri: Double,
    /** null quando RI = 0 (n ≤ 2). */
    val rc: Double?,
)

/**
 * AHP pela média geométrica normalizada (DEC-018, PDF AHP+TOPSIS).
 * λmax = média de (Aw)i / wi; IC = (λmax − n)/(n − 1); RC = IC/RI.
 * Função pura e síncrona: o caso de uso decide o dispatcher e o limiar.
 */
object AhpEngine {
    /** Índices aleatórios de Saaty usados no PDF de referência (n = 1..10). */
    val INDICES_ALEATORIOS = mapOf(
        1 to 0.0, 2 to 0.0, 3 to 0.58, 4 to 0.90, 5 to 1.12,
        6 to 1.24, 7 to 1.32, 8 to 1.41, 9 to 1.45, 10 to 1.49,
    )

    fun calcular(matriz: List<List<Double>>): SaidaAhp {
        val n = matriz.size
        if (n == 0 || matriz.any { it.size != n }) throw FalhaMatematica("A matriz AHP deve ser quadrada e não vazia.")
        val ri = INDICES_ALEATORIOS[n] ?: throw FalhaMatematica("Índice aleatório não suportado para n=$n (máximo 10).")
        for (i in 0 until n) for (j in 0 until n) {
            val a = matriz[i][j]
            if (!a.isFinite() || a <= 0.0) throw FalhaMatematica("Julgamentos AHP devem ser positivos e finitos.")
        }
        for (i in 0 until n) {
            if (abs(matriz[i][i] - 1.0) > 1e-12) throw FalhaMatematica("A diagonal da matriz AHP deve conter 1.")
            for (j in i + 1 until n) {
                if (abs(matriz[i][j] * matriz[j][i] - 1.0) > 1e-9) {
                    throw FalhaMatematica("A matriz AHP deve ser recíproca: a[j][i] = 1 / a[i][j].")
                }
            }
        }

        // Média geométrica por logaritmos evita overflow do produto.
        val medias = (0 until n).map { i -> exp((0 until n).sumOf { j -> ln(matriz[i][j]) } / n) }
        val soma = medias.sum()
        if (!soma.isFinite() || soma <= 0.0) throw FalhaMatematica("Soma das médias geométricas inválida.")
        val pesos = medias.map { it / soma }
        if (pesos.any { !it.isFinite() || it <= 0.0 }) throw FalhaMatematica("Pesos AHP não finitos ou não positivos.")

        val aw = (0 until n).map { i -> (0 until n).sumOf { j -> matriz[i][j] * pesos[j] } }
        val lambdaMax = (0 until n).map { aw[it] / pesos[it] }.average()
        val ic = if (n <= 1) 0.0 else (lambdaMax - n) / (n - 1)
        val rc = if (ri == 0.0) null else ic / ri
        if (!lambdaMax.isFinite() || !ic.isFinite() || (rc != null && !rc.isFinite())) {
            throw FalhaMatematica("Indicadores AHP não finitos.")
        }
        return SaidaAhp(pesos, lambdaMax, ic, ri, rc)
    }
}
