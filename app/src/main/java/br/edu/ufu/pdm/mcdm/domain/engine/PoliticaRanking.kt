package br.edu.ufu.pdm.mcdm.domain.engine

import kotlin.math.abs

/**
 * Empates e posições (DEC-027/028): ordena C* decrescente; cada grupo inicia no maior coeficiente
 * ainda não agrupado e inclui valores com diferença absoluta ≤ 10⁻⁹ em relação a ele, sem encadear
 * vizinhos. Posições competitivas: 1, 2, 2, 4. Não há desempate por índice, ID ou nome.
 */
object PoliticaRanking {
    const val TOLERANCIA_EMPATE = 1e-9

    /** Retorna a posição de cada alternativa, na mesma ordem dos coeficientes recebidos. */
    fun posicoes(coeficientes: List<Double>): List<Int> {
        val ordem = coeficientes.indices.sortedByDescending { coeficientes[it] }
        val posicoes = IntArray(coeficientes.size)
        var k = 0
        while (k < ordem.size) {
            val referencia = coeficientes[ordem[k]]
            val posicao = k + 1
            var fim = k
            while (fim < ordem.size && abs(referencia - coeficientes[ordem[fim]]) <= TOLERANCIA_EMPATE) {
                posicoes[ordem[fim]] = posicao
                fim++
            }
            k = fim
        }
        return posicoes.toList()
    }
}
