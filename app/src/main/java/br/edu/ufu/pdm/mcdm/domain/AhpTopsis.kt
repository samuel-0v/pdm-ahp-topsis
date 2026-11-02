package br.edu.ufu.pdm.mcdm.domain

import kotlin.math.pow
import kotlin.math.sqrt

/** Natureza de um critério para a definição das soluções ideais do TOPSIS. */
enum class CriterionType { BENEFIT, COST }

data class AhpResult(
    val weights: List<Double>,
    val lambdaMax: Double,
    val consistencyIndex: Double,
    val randomIndex: Double,
    val consistencyRatio: Double?,
    val isConsistent: Boolean,
)

data class TopsisAlternativeResult(
    val alternativeIndex: Int,
    val distanceToPositiveIdeal: Double,
    val distanceToNegativeIdeal: Double,
    val closeness: Double,
)

data class TopsisResult(val ranking: List<TopsisAlternativeResult>)

/** Pure Kotlin implementation of the AHP and TOPSIS calculations in the assignment. */
object AhpTopsis {
    private val randomIndices = mapOf(
        1 to 0.0, 2 to 0.0, 3 to 0.58, 4 to 0.90, 5 to 1.12,
        6 to 1.24, 7 to 1.32, 8 to 1.41, 9 to 1.45, 10 to 1.49,
    )

    /**
     * Calculates AHP priorities by geometric means, as specified in the companion PDF.
     * The pairwise matrix must be positive, square, and reciprocal.
     */
    fun calculateAhp(matrix: List<List<Double>>, consistencyThreshold: Double = 0.10): AhpResult {
        require(consistencyThreshold in 0.0..1.0) { "O limite de consistência deve estar entre 0 e 1." }
        val n = matrix.size
        require(n > 0 && matrix.all { it.size == n }) { "A matriz AHP deve ser quadrada e não vazia." }
        require(matrix.all { row -> row.all { it.isFinite() && it > 0.0 } }) {
            "Todos os julgamentos AHP devem ser números positivos e finitos."
        }
        for (i in 0 until n) {
            require(kotlin.math.abs(matrix[i][i] - 1.0) <= 1e-9) { "A diagonal da matriz AHP deve conter 1." }
            for (j in 0 until n) {
                require(kotlin.math.abs(matrix[i][j] * matrix[j][i] - 1.0) <= 1e-7) {
                    "A matriz AHP deve ser recíproca: a[j][i] = 1 / a[i][j]."
                }
            }
        }

        val geometricMeans = (0 until n).map { i ->
            kotlin.math.exp((0 until n).sumOf { j -> kotlin.math.ln(matrix[i][j]) } / n)
        }
        val total = geometricMeans.sum()
        val weights = geometricMeans.map { it / total }
        val weightedSums = (0 until n).map { i ->
            (0 until n).sumOf { j -> matrix[i][j] * weights[j] }
        }
        val lambdaMax = (0 until n).map { weightedSums[it] / weights[it] }.average()
        val consistencyIndex = if (n <= 1) 0.0 else (lambdaMax - n) / (n - 1)
        val randomIndex = randomIndices[n]
            ?: throw IllegalArgumentException("Índice aleatório não configurado para n=$n (máximo 10).")
        // RI=0 for n=1,2: consistency is not defined and should not be misreported as a measured zero.
        val consistencyRatio = if (randomIndex == 0.0) null else consistencyIndex / randomIndex
        val isConsistent = consistencyRatio == null || consistencyRatio <= consistencyThreshold

        return AhpResult(weights, lambdaMax, consistencyIndex, randomIndex, consistencyRatio, isConsistent)
    }

    /** Ranks alternatives using vector normalization and AHP-derived criterion weights. */
    fun calculateTopsis(
        decisionMatrix: List<List<Double>>,
        weights: List<Double>,
        criterionTypes: List<CriterionType>,
    ): TopsisResult {
        val alternatives = decisionMatrix.size
        val criteria = weights.size
        require(alternatives >= 2) { "O TOPSIS requer pelo menos duas alternativas." }
        require(criteria > 0 && decisionMatrix.all { it.size == criteria }) {
            "A matriz de decisão deve ter uma coluna por critério/peso."
        }
        require(criterionTypes.size == criteria) { "Informe um tipo para cada critério." }
        require(weights.all { it.isFinite() && it >= 0.0 } && weights.sum() > 0.0) {
            "Os pesos devem ser finitos, não negativos e ter soma maior que zero."
        }
        require(decisionMatrix.all { row -> row.all { it.isFinite() && it >= 0.0 } }) {
            "Os desempenhos devem ser finitos e não negativos."
        }

        val normalizedWeights = weights.map { it / weights.sum() }
        val denominators = (0 until criteria).map { j ->
            sqrt((0 until alternatives).sumOf { i -> decisionMatrix[i][j].pow(2) })
        }
        require(denominators.all { it > 0.0 }) { "Cada critério deve variar ou ter valores diferentes de zero." }

        val weighted = List(alternatives) { i ->
            List(criteria) { j -> decisionMatrix[i][j] / denominators[j] * normalizedWeights[j] }
        }
        val positiveIdeal = List(criteria) { j ->
            val column = (0 until alternatives).map { weighted[it][j] }
            if (criterionTypes[j] == CriterionType.BENEFIT) column.max() else column.min()
        }
        val negativeIdeal = List(criteria) { j ->
            val column = (0 until alternatives).map { weighted[it][j] }
            if (criterionTypes[j] == CriterionType.BENEFIT) column.min() else column.max()
        }

        val results = (0 until alternatives).map { i ->
            val dPositive = sqrt((0 until criteria).sumOf { j -> (weighted[i][j] - positiveIdeal[j]).pow(2) })
            val dNegative = sqrt((0 until criteria).sumOf { j -> (weighted[i][j] - negativeIdeal[j]).pow(2) })
            val distanceSum = dPositive + dNegative
            val closeness = if (distanceSum == 0.0) 0.0 else dNegative / distanceSum
            TopsisAlternativeResult(i, dPositive, dNegative, closeness)
        }.sortedWith(compareByDescending<TopsisAlternativeResult> { it.closeness }.thenBy { it.alternativeIndex })

        return TopsisResult(results)
    }
}
