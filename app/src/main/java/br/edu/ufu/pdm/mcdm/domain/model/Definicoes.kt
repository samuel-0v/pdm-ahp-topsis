package br.edu.ufu.pdm.mcdm.domain.model

import java.time.Instant

/** Natureza do critério para as soluções ideais do TOPSIS. */
enum class Natureza { BENEFICIO, CUSTO }

/** Variantes da união tipada aprovada na DEC-041. Ausência é representada por `null`. */
enum class TipoValor { MONETARIO_BRL, NUMERO }

/** Origem de um produto. Identidade histórica = origem + ID (DEC-034). */
enum class OrigemProduto { OFICIAL, PESSOAL }

/** Valor canônico. Dinheiro é Long em centavos; nunca passa por Double antes de ser preservado. */
sealed interface ValorCanonico {
    val tipo: TipoValor

    data class Monetario(val centavos: Long) : ValorCanonico {
        override val tipo get() = TipoValor.MONETARIO_BRL
    }

    data class Numero(val valor: Double) : ValorCanonico {
        init {
            require(valor.isFinite()) { "Número não finito." }
        }

        override val tipo get() = TipoValor.NUMERO
    }
}

/** Metadados genéricos de validação de um critério (DEC-033). Limites no mesmo tipo do valor. */
data class Validacao(
    val tipoValor: TipoValor,
    val obrigatorioCadastro: Boolean,
    val obrigatorioProcessamento: Boolean,
    val exigeInteiro: Boolean,
    val minimo: ValorCanonico? = null,
    val minimoInclusivo: Boolean = true,
    val maximo: ValorCanonico? = null,
    val maximoInclusivo: Boolean = true,
    val casasDecimais: Int? = null,
) {
    /** Moeda e escala fixas da variante monetária (DEC-041). */
    val moeda: String? get() = if (tipoValor == TipoValor.MONETARIO_BRL) "BRL" else null
    val escala: Int? get() = if (tipoValor == TipoValor.MONETARIO_BRL) 2 else null
}

/**
 * Definição de critério. É copiada para produtos pessoais e para cada avaliação.
 * Peso AHP não faz parte da definição (DEC-039).
 */
data class CriterioDefinicao(
    val id: String,
    val nome: String,
    val ordem: Int,
    val unidade: String,
    val natureza: Natureza,
    val validacao: Validacao,
    val descricao: String? = null,
)

data class Categoria(
    val id: String,
    val nome: String,
    val descricao: String?,
    val revisaoDefinicao: String,
    val criterios: List<CriterioDefinicao>,
    val atualizadoEm: Instant?,
) {
    val criteriosOrdenados: List<CriterioDefinicao> get() = criterios.sortedBy { it.ordem }
}

/** Proveniência por valor. Desconhecido permanece `null` (não é falso, zero nem data atual). */
data class Proveniencia(
    val fonte: String? = null,
    val dataReferencia: String? = null,
    val protocoloOuRubrica: String? = null,
    val estimativa: Boolean? = null,
    /** Marca valores efetivos ajustados pelo usuário em uma avaliação. */
    val ajustadoNaAvaliacao: Boolean = false,
) {
    val vazia: Boolean
        get() = fonte == null && dataReferencia == null && protocoloOuRubrica == null && estimativa == null && !ajustadoNaAvaliacao

    companion object {
        val DESCONHECIDA = Proveniencia()
        val AJUSTE = Proveniencia(ajustadoNaAvaliacao = true)
    }
}

/** Unidades: código estável -> rótulo de exibição. Código desconhecido é exibido como recebido. */
object Unidades {
    private val rotulos = mapOf(
        "BRL" to "R$",
        "BRL_POR_ANO" to "R$/ano",
        "HORAS" to "h",
        "KM" to "km",
        "MINUTOS" to "min",
        "GB" to "GB",
        "PONTOS" to "pontos",
        "NOTA_1_10" to "nota 1–10",
    )

    fun rotulo(codigo: String): String = rotulos[codigo] ?: codigo
}
