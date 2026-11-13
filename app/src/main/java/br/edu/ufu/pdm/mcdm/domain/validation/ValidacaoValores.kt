package br.edu.ufu.pdm.mcdm.domain.validation

import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.TipoValor
import br.edu.ufu.pdm.mcdm.domain.model.Validacao
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import java.math.BigDecimal
import java.math.RoundingMode

sealed interface ResultadoEntrada {
    /** Campo vazio: ausência real, nunca zero. */
    data object Vazio : ResultadoEntrada
    data class Valido(val valor: ValorCanonico) : ResultadoEntrada
    data class Invalido(val mensagem: String) : ResultadoEntrada
}

/**
 * Interpretação de texto digitado no contexto pt-BR (DEC-041):
 * vírgula é separador decimal; ponto só é aceito como separador de milhar em grupos de três
 * dígitos (1.234.567,89). "5.5" é ambíguo e é rejeitado com explicação.
 * Análise decimal exata (BigDecimal) antes de obter centavos; frações de centavo são rejeitadas.
 */
object EntradaPtBr {
    private val milharValido = Regex("""^\d{1,3}(\.\d{3})+$""")
    private val semMilhar = Regex("""^\d+$""")

    fun interpretarDecimal(texto: String): BigDecimal? {
        var t = texto.trim().replace("R$", "").replace(" ", "").replace(" ", "")
        if (t.isEmpty()) return null
        var negativo = false
        if (t.startsWith("-")) { negativo = true; t = t.substring(1) }
        if (t.count { it == ',' } > 1) throw IllegalArgumentException("Use apenas uma vírgula decimal.")
        val partes = t.split(',')
        val inteira = partes[0]
        val fracao = partes.getOrNull(1)
        if (inteira.isEmpty()) throw IllegalArgumentException("Informe a parte inteira antes da vírgula.")
        val inteiraLimpa = when {
            semMilhar.matches(inteira) -> inteira
            milharValido.matches(inteira) -> inteira.replace(".", "")
            inteira.contains('.') -> throw IllegalArgumentException("Use vírgula para decimais; ponto só separa milhares (1.234,56).")
            else -> throw IllegalArgumentException("Valor numérico inválido.")
        }
        if (fracao != null && (fracao.isEmpty() || !semMilhar.matches(fracao))) {
            throw IllegalArgumentException("Casas decimais inválidas.")
        }
        val bd = BigDecimal(if (fracao != null) "$inteiraLimpa.$fracao" else inteiraLimpa)
        return if (negativo) bd.negate() else bd
    }

    /** Converte o texto para o valor canônico do tipo do critério e valida os metadados. */
    fun interpretar(texto: String, criterio: CriterioDefinicao): ResultadoEntrada {
        val decimal = try {
            interpretarDecimal(texto) ?: return ResultadoEntrada.Vazio
        } catch (e: IllegalArgumentException) {
            return ResultadoEntrada.Invalido(e.message ?: "Valor inválido.")
        }
        val valor = when (criterio.validacao.tipoValor) {
            TipoValor.MONETARIO_BRL -> {
                val centavos = try {
                    decimal.movePointRight(2).setScale(0, RoundingMode.UNNECESSARY)
                } catch (_: ArithmeticException) {
                    return ResultadoEntrada.Invalido("Fração de centavo não é aceita; use no máximo duas casas significativas.")
                }
                val longo = try {
                    centavos.longValueExact()
                } catch (_: ArithmeticException) {
                    return ResultadoEntrada.Invalido("Valor fora da faixa representável.")
                }
                ValorCanonico.Monetario(longo)
            }
            TipoValor.NUMERO -> {
                val d = decimal.toDouble()
                if (!d.isFinite()) return ResultadoEntrada.Invalido("Valor fora da faixa representável.")
                ValorCanonico.Numero(d)
            }
        }
        return when (val erro = ValidadorValor.validar(valor, criterio.validacao)) {
            null -> ResultadoEntrada.Valido(valor)
            else -> ResultadoEntrada.Invalido(erro)
        }
    }
}

object ValidadorValor {
    /** Retorna mensagem de erro ou null se o valor satisfaz os metadados conhecidos. */
    fun validar(valor: ValorCanonico, v: Validacao): String? {
        if (valor.tipo != v.tipoValor) return "Tipo do valor incompatível com o critério."
        when (valor) {
            is ValorCanonico.Numero -> {
                if (!valor.valor.isFinite()) return "Valor não finito."
                if (v.exigeInteiro && valor.valor != kotlin.math.floor(valor.valor)) return "Informe um número inteiro."
                v.casasDecimais?.let { casas ->
                    val escala = BigDecimal(valor.valor.toString()).stripTrailingZeros().scale()
                    if (escala > casas) return "Use no máximo $casas casas decimais."
                }
            }
            is ValorCanonico.Monetario -> Unit
        }
        val abaixo = v.minimo?.let { comparar(valor, it)?.let { c -> if (v.minimoInclusivo) c < 0 else c <= 0 } } ?: false
        val acima = v.maximo?.let { comparar(valor, it)?.let { c -> if (v.maximoInclusivo) c > 0 else c >= 0 } } ?: false
        if (abaixo || acima) return "Valor fora do intervalo ${descreverLimites(v)}."
        return null
    }

    /** Comparação exata no mesmo tipo: centavos com centavos (sem passar por Double). */
    private fun comparar(a: ValorCanonico, b: ValorCanonico): Int? = when {
        a is ValorCanonico.Monetario && b is ValorCanonico.Monetario -> a.centavos.compareTo(b.centavos)
        a is ValorCanonico.Numero && b is ValorCanonico.Numero -> a.valor.compareTo(b.valor)
        else -> null
    }

    fun descreverLimites(v: Validacao): String {
        val min = v.minimo?.let { (if (v.minimoInclusivo) "[" else "(") + Formatos.valorSemUnidade(it) } ?: "(−∞"
        val max = v.maximo?.let { Formatos.valorSemUnidade(it) + (if (v.maximoInclusivo) "]" else ")") } ?: "+∞)"
        return "$min; $max"
    }
}
