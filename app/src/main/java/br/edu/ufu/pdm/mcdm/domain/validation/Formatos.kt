package br.edu.ufu.pdm.mcdm.domain.validation

import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.TipoValor
import br.edu.ufu.pdm.mcdm.domain.model.Unidades
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/** Formatação pt-BR para exibição. Não altera valores internos nem limites de consistência. */
object Formatos {
    private val ptBr = Locale.forLanguageTag("pt-BR")
    private val simbolos = DecimalFormatSymbols(ptBr)

    private fun formato(padrao: String) = DecimalFormat(padrao, simbolos)

    fun centavos(centavos: Long): String {
        val bd = BigDecimal.valueOf(centavos).movePointLeft(2)
        return "R$ " + formato("#,##0.00").format(bd)
    }

    fun numero(valor: Double, maxCasas: Int = 4): String {
        val v = if (valor == 0.0) 0.0 else valor // evita "-0"
        return formato("#,##0." + "#".repeat(maxCasas)).format(v).let { if (it == "-0") "0" else it }
    }

    fun fixo(valor: Double, casas: Int): String {
        val texto = formato("0." + "0".repeat(casas)).format(valor)
        return if (texto.matches(Regex("""-0,0+"""))) texto.removePrefix("-") else texto
    }

    fun percentual(peso: Double): String = formato("0.0").format(peso * 100) + "%"

    fun valorSemUnidade(valor: ValorCanonico): String = when (valor) {
        is ValorCanonico.Monetario -> centavos(valor.centavos)
        is ValorCanonico.Numero -> numero(valor.valor)
    }

    fun valor(valor: ValorCanonico?, criterio: CriterioDefinicao): String {
        if (valor == null) return "não informado"
        return when (valor) {
            is ValorCanonico.Monetario ->
                centavos(valor.centavos) + if (criterio.unidade == "BRL_POR_ANO") "/ano" else ""
            is ValorCanonico.Numero -> numero(valor.valor) + " " + Unidades.rotulo(criterio.unidade)
        }
    }

    /** Texto editável correspondente a um valor canônico (vírgula decimal, sem milhar). */
    fun paraEdicao(valor: ValorCanonico?): String = when (valor) {
        null -> ""
        is ValorCanonico.Monetario -> BigDecimal.valueOf(valor.centavos).movePointLeft(2).toPlainString().replace('.', ',')
        is ValorCanonico.Numero -> BigDecimal(valor.valor.toString()).stripTrailingZeros().toPlainString().replace('.', ',')
    }

    /**
     * RC exibida com casas suficientes para não mostrar como aceita uma RC rejeitada
     * (ex.: 0,1000001 não pode aparecer como 0,1000).
     */
    fun rc(rc: Double): String {
        for (casas in 4..12) {
            val texto = fixo(rc, casas)
            val arredondado = texto.replace(',', '.').toDouble()
            val ladoCorreto = (rc <= 0.10) == (arredondado <= 0.10)
            if (ladoCorreto) return texto
        }
        return rc.toString().replace('.', ',')
    }

    fun rotuloTipo(tipo: TipoValor) = if (tipo == TipoValor.MONETARIO_BRL) "monetário (R$)" else "número"
}
