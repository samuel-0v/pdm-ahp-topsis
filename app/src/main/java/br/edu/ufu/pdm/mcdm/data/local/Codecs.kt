package br.edu.ufu.pdm.mcdm.data.local

import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.Proveniencia
import br.edu.ufu.pdm.mcdm.domain.model.TipoValor
import br.edu.ufu.pdm.mcdm.domain.model.Validacao
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Representação inválida de dado salvo: não é reparada nem tratada como ausência. */
class RepresentacaoInvalidaException(mensagem: String) : Exception(mensagem)

/**
 * Codecs do JSON local (contrato 03). Long permanece inteiro decimal exato; Double usa a
 * representação mais curta que recupera o mesmo binário (Double.toString); null permanece null.
 */
object Codecs {
    const val FORMATO_PAYLOAD = 1

    val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = false
    }

    @Serializable
    data class ValorDto(val tipo: String, val centavos: Long? = null, val numero: Double? = null)

    @Serializable
    data class ValidacaoDto(
        val tipoValor: String,
        val obrigatorioCadastro: Boolean,
        val obrigatorioProcessamento: Boolean,
        val exigeInteiro: Boolean,
        val minimo: ValorDto? = null,
        val minimoInclusivo: Boolean = true,
        val maximo: ValorDto? = null,
        val maximoInclusivo: Boolean = true,
        val casasDecimais: Int? = null,
    )

    @Serializable
    data class CriterioDto(
        val id: String,
        val nome: String,
        val ordem: Int,
        val unidade: String,
        val natureza: String,
        val validacao: ValidacaoDto,
        val descricao: String? = null,
    )

    @Serializable
    data class ProvenienciaDto(
        val fonte: String? = null,
        val dataReferencia: String? = null,
        val protocoloOuRubrica: String? = null,
        val estimativa: Boolean? = null,
        val ajustadoNaAvaliacao: Boolean = false,
    )

    @Serializable
    data class PayloadProdutoPessoal(
        val formatoPayload: Int,
        val definicoes: List<CriterioDto>,
        val valores: Map<String, ValorDto>,
        val referencias: Map<String, ProvenienciaDto> = emptyMap(),
    )

    // ----- valores -----

    fun valorParaDto(v: ValorCanonico): ValorDto = when (v) {
        is ValorCanonico.Monetario -> ValorDto(TipoValor.MONETARIO_BRL.name, centavos = v.centavos)
        is ValorCanonico.Numero -> ValorDto(TipoValor.NUMERO.name, numero = v.valor)
    }

    fun valorDeDto(d: ValorDto): ValorCanonico = when (d.tipo) {
        TipoValor.MONETARIO_BRL.name -> {
            if (d.centavos == null || d.numero != null) throw RepresentacaoInvalidaException("Envelope monetário contraditório.")
            ValorCanonico.Monetario(d.centavos)
        }
        TipoValor.NUMERO.name -> {
            if (d.numero == null || d.centavos != null || !d.numero.isFinite()) throw RepresentacaoInvalidaException("Envelope numérico contraditório.")
            ValorCanonico.Numero(d.numero)
        }
        else -> throw RepresentacaoInvalidaException("Tipo de valor desconhecido: ${d.tipo}.")
    }

    /** Colunas Room de um grupo tipado (`origem` ou `efetivo`). */
    data class Colunas(val tipo: String?, val centavos: Long?, val numero: Double?)

    fun paraColunas(v: ValorCanonico?): Colunas = when (v) {
        null -> Colunas(null, null, null)
        is ValorCanonico.Monetario -> Colunas(TipoValor.MONETARIO_BRL.name, v.centavos, null)
        is ValorCanonico.Numero -> Colunas(TipoValor.NUMERO.name, null, v.valor)
    }

    fun deColunas(tipo: String?, centavos: Long?, numero: Double?): ValorCanonico? = when (tipo) {
        null -> {
            if (centavos != null || numero != null) throw RepresentacaoInvalidaException("Valor sem discriminador.")
            null
        }
        else -> valorDeDto(ValorDto(tipo, centavos, numero))
    }

    // ----- definições -----

    fun criterioParaDto(c: CriterioDefinicao) = CriterioDto(
        id = c.id, nome = c.nome, ordem = c.ordem, unidade = c.unidade, natureza = c.natureza.name,
        validacao = validacaoParaDto(c.validacao), descricao = c.descricao,
    )

    fun criterioDeDto(d: CriterioDto) = CriterioDefinicao(
        id = d.id, nome = d.nome, ordem = d.ordem, unidade = d.unidade,
        natureza = enumOuErro<Natureza>(d.natureza), validacao = validacaoDeDto(d.validacao), descricao = d.descricao,
    )

    fun validacaoParaDto(v: Validacao) = ValidacaoDto(
        tipoValor = v.tipoValor.name,
        obrigatorioCadastro = v.obrigatorioCadastro,
        obrigatorioProcessamento = v.obrigatorioProcessamento,
        exigeInteiro = v.exigeInteiro,
        minimo = v.minimo?.let(::valorParaDto), minimoInclusivo = v.minimoInclusivo,
        maximo = v.maximo?.let(::valorParaDto), maximoInclusivo = v.maximoInclusivo,
        casasDecimais = v.casasDecimais,
    )

    fun validacaoDeDto(d: ValidacaoDto) = Validacao(
        tipoValor = enumOuErro<TipoValor>(d.tipoValor),
        obrigatorioCadastro = d.obrigatorioCadastro,
        obrigatorioProcessamento = d.obrigatorioProcessamento,
        exigeInteiro = d.exigeInteiro,
        minimo = d.minimo?.let(::valorDeDto), minimoInclusivo = d.minimoInclusivo,
        maximo = d.maximo?.let(::valorDeDto), maximoInclusivo = d.maximoInclusivo,
        casasDecimais = d.casasDecimais,
    )

    fun validacaoParaJson(v: Validacao): String = json.encodeToString(ValidacaoDto.serializer(), validacaoParaDto(v))
    fun validacaoDeJson(s: String): Validacao = decodificar { validacaoDeDto(json.decodeFromString(ValidacaoDto.serializer(), s)) }

    // ----- proveniência -----

    fun provParaDto(p: Proveniencia) = ProvenienciaDto(p.fonte, p.dataReferencia, p.protocoloOuRubrica, p.estimativa, p.ajustadoNaAvaliacao)
    fun provDeDto(d: ProvenienciaDto) = Proveniencia(d.fonte, d.dataReferencia, d.protocoloOuRubrica, d.estimativa, d.ajustadoNaAvaliacao)

    fun provParaJson(p: Proveniencia): String? = if (p.vazia) null else json.encodeToString(ProvenienciaDto.serializer(), provParaDto(p))
    fun provDeJson(s: String?): Proveniencia =
        if (s == null) Proveniencia.DESCONHECIDA else decodificar { provDeDto(json.decodeFromString(ProvenienciaDto.serializer(), s)) }

    // ----- payload do produto pessoal -----

    fun payloadParaJson(definicoes: List<CriterioDefinicao>, valores: Map<String, ValorCanonico>, referencias: Map<String, Proveniencia>): String =
        json.encodeToString(
            PayloadProdutoPessoal.serializer(),
            PayloadProdutoPessoal(
                formatoPayload = FORMATO_PAYLOAD,
                definicoes = definicoes.map(::criterioParaDto),
                valores = valores.mapValues { valorParaDto(it.value) },
                referencias = referencias.filterValues { !it.vazia }.mapValues { provParaDto(it.value) },
            ),
        )

    data class Payload(val definicoes: List<CriterioDefinicao>, val valores: Map<String, ValorCanonico>, val referencias: Map<String, Proveniencia>)

    fun payloadDeJson(s: String, formatoColuna: Int): Payload = decodificar {
        if (formatoColuna != FORMATO_PAYLOAD) throw RepresentacaoInvalidaException("formatoPayload $formatoColuna desconhecido.")
        val p = json.decodeFromString(PayloadProdutoPessoal.serializer(), s)
        if (p.formatoPayload != formatoColuna) throw RepresentacaoInvalidaException("formatoPayload divergente.")
        val defs = p.definicoes.map(::criterioDeDto)
        val ids = defs.map { it.id }.toSet()
        if (!ids.containsAll(p.valores.keys)) throw RepresentacaoInvalidaException("Valor de critério desconhecido no payload.")
        Payload(defs, p.valores.mapValues { valorDeDto(it.value) }, p.referencias.mapValues { provDeDto(it.value) })
    }

    private inline fun <T> decodificar(bloco: () -> T): T = try {
        bloco()
    } catch (e: RepresentacaoInvalidaException) {
        throw e
    } catch (e: Exception) {
        throw RepresentacaoInvalidaException("JSON local inválido: ${e.message}")
    }

    inline fun <reified E : Enum<E>> enumOuErro(nome: String): E =
        enumValues<E>().firstOrNull { it.name == nome } ?: throw RepresentacaoInvalidaException("Código desconhecido: $nome.")
}
