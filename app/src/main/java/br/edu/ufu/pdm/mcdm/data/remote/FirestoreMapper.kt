package br.edu.ufu.pdm.mcdm.data.remote

import br.edu.ufu.pdm.mcdm.domain.model.Categoria
import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.IdentificacaoProduto
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoOficial
import br.edu.ufu.pdm.mcdm.domain.model.Proveniencia
import br.edu.ufu.pdm.mcdm.domain.model.TipoValor
import br.edu.ufu.pdm.mcdm.domain.model.Validacao
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import br.edu.ufu.pdm.mcdm.domain.model.ValorLido
import com.google.firebase.Timestamp
import java.time.Instant

class DocumentoInvalidoException(mensagem: String) : Exception(mensagem)

/**
 * Interpreta documentos do contrato DEC-040 a partir de mapas (independente do SDK para testes).
 * Firestore entrega integerValue como Long e doubleValue como Double: `centavos` precisa ser Long.
 */
object FirestoreMapper {
    const val FORMATO_CONTRATO = 1L
    private const val MAX_INTEIRO_EXATO = 9007199254740992L // 2^53

    fun categoria(id: String, d: Map<String, Any?>): Categoria {
        if (id.isBlank()) throw DocumentoInvalidoException("ID de categoria vazio.")
        if (d["formatoContrato"] != FORMATO_CONTRATO) throw DocumentoInvalidoException("formatoContrato desconhecido.")
        val nome = texto(d, "nome")
        val revisao = texto(d, "revisaoDefinicao")
        val lista = d["criterios"] as? List<*> ?: throw DocumentoInvalidoException("criterios ausente.")
        val criterios = lista.map { criterio(it as? Map<*, *> ?: throw DocumentoInvalidoException("Critério inválido.")) }
        if (criterios.map { it.id }.toSet().size != criterios.size) throw DocumentoInvalidoException("IDs de critério repetidos.")
        if (criterios.map { it.ordem }.toSet().size != criterios.size) throw DocumentoInvalidoException("Ordens de critério repetidas.")
        if (criterios.isEmpty() || criterios.size > 10) throw DocumentoInvalidoException("Quantidade de critérios não suportada.")
        return Categoria(id, nome, d["descricao"] as? String, revisao, criterios.sortedBy { it.ordem }, instante(d["atualizadoEm"]))
    }

    private fun criterio(m: Map<*, *>): CriterioDefinicao {
        @Suppress("UNCHECKED_CAST")
        val d = m as Map<String, Any?>
        val ordem = d["ordem"] as? Long ?: throw DocumentoInvalidoException("ordem deve ser inteira.")
        if (ordem < 0 || ordem > Int.MAX_VALUE) throw DocumentoInvalidoException("ordem fora da faixa.")
        val natureza = when (d["natureza"]) {
            "BENEFICIO" -> Natureza.BENEFICIO
            "CUSTO" -> Natureza.CUSTO
            else -> throw DocumentoInvalidoException("natureza desconhecida.")
        }
        @Suppress("UNCHECKED_CAST")
        val v = d["validacao"] as? Map<String, Any?> ?: throw DocumentoInvalidoException("validacao ausente.")
        val tipo = when (v["tipoValor"]) {
            "MONETARIO_BRL" -> TipoValor.MONETARIO_BRL
            "NUMERO" -> TipoValor.NUMERO
            else -> throw DocumentoInvalidoException("tipoValor desconhecido.")
        }
        if (tipo == TipoValor.MONETARIO_BRL && (v["moeda"] != "BRL" || v["escala"] != 2L)) {
            throw DocumentoInvalidoException("Monetário exige moeda BRL e escala 2.")
        }
        @Suppress("UNCHECKED_CAST")
        val limites = v["limites"] as? Map<String, Any?>
        return CriterioDefinicao(
            id = texto(d, "id"),
            nome = texto(d, "nome"),
            ordem = ordem.toInt(),
            unidade = texto(d, "unidade"),
            natureza = natureza,
            validacao = Validacao(
                tipoValor = tipo,
                obrigatorioCadastro = booleano(v, "obrigatorioCadastro"),
                obrigatorioProcessamento = booleano(v, "obrigatorioProcessamento"),
                exigeInteiro = booleano(v, "exigeInteiro"),
                minimo = limites?.get("minimo")?.let { limite(it, tipo) },
                minimoInclusivo = limites?.get("minimoInclusivo") as? Boolean ?: true,
                maximo = limites?.get("maximo")?.let { limite(it, tipo) },
                maximoInclusivo = limites?.get("maximoInclusivo") as? Boolean ?: true,
                casasDecimais = (v["casasDecimais"] as? Long)?.toInt(),
            ),
            descricao = d["descricao"] as? String,
        )
    }

    private fun limite(x: Any, tipo: TipoValor): ValorCanonico = when (tipo) {
        TipoValor.MONETARIO_BRL -> ValorCanonico.Monetario(x as? Long ?: throw DocumentoInvalidoException("Limite monetário deve ser inteiro (centavos)."))
        TipoValor.NUMERO -> ValorCanonico.Numero(numeroExato(x) ?: throw DocumentoInvalidoException("Limite numérico inválido."))
    }

    fun produto(id: String, d: Map<String, Any?>, categorias: Map<String, Categoria>): ProdutoOficial {
        if (d["formatoContrato"] != FORMATO_CONTRATO) throw DocumentoInvalidoException("formatoContrato desconhecido.")
        val categoriaId = texto(d, "categoriaId")
        val problemas = mutableListOf<String>()
        val criterios = categorias[categoriaId]?.criterios?.associateBy { it.id }

        @Suppress("UNCHECKED_CAST")
        val brutos = d["valores"] as? Map<String, Any?> ?: emptyMap()
        val valores = mutableMapOf<String, ValorLido>()
        for ((criterioId, bruto) in brutos) {
            val c = criterios?.get(criterioId)
            if (criterios != null && c == null) {
                problemas += "Critério desconhecido: $criterioId."
                continue
            }
            valores[criterioId] = valor(bruto, c?.validacao?.tipoValor)
        }
        criterios?.keys?.forEach { if (it !in valores) valores[it] = ValorLido.Ausente }

        @Suppress("UNCHECKED_CAST")
        val refs = (d["referenciasPorCriterio"] as? Map<String, Any?>).orEmpty().mapNotNull { (k, v) ->
            val m = v as? Map<*, *> ?: return@mapNotNull null
            k to Proveniencia(
                fonte = m["fonte"] as? String,
                dataReferencia = m["dataReferencia"] as? String,
                protocoloOuRubrica = m["protocoloOuRubrica"] as? String,
                estimativa = m["estimativa"] as? Boolean,
            )
        }.toMap()

        return ProdutoOficial(
            id = id,
            categoriaId = categoriaId,
            revisaoDefinicao = texto(d, "revisaoDefinicao"),
            identificacao = IdentificacaoProduto(
                nome = texto(d, "nome"),
                fabricante = d["fabricante"] as? String,
                modelo = d["modelo"] as? String,
                versao = d["versao"] as? String,
                ano = (d["ano"] as? Long)?.toInt(),
            ),
            valores = valores,
            referencias = refs,
            atualizadoEm = instante(d["atualizadoEm"]),
            problemas = problemas,
        )
    }

    /** Envelope tipado (contrato 03). Ausência = null; formato fora do contrato = Incompativel. */
    fun valor(bruto: Any?, esperado: TipoValor?): ValorLido {
        if (bruto == null) return ValorLido.Ausente
        val m = bruto as? Map<*, *> ?: return ValorLido.Incompativel("valor fora do envelope tipado")
        return when (m["tipo"]) {
            "MONETARIO_BRL" -> {
                if (esperado != null && esperado != TipoValor.MONETARIO_BRL) return ValorLido.Incompativel("variante monetária em critério numérico")
                if (m.containsKey("numero")) return ValorLido.Incompativel("mais de uma variante preenchida")
                if (m["moeda"] != "BRL" || m["escala"] != 2L) return ValorLido.Incompativel("moeda/escala contraditórias")
                when (val c = m["centavos"]) {
                    is Long -> ValorLido.Valido(ValorCanonico.Monetario(c))
                    is Double -> ValorLido.Incompativel("centavos em ponto flutuante")
                    else -> ValorLido.Incompativel("centavos ausente ou não inteiro")
                }
            }
            "NUMERO" -> {
                if (esperado != null && esperado != TipoValor.NUMERO) return ValorLido.Incompativel("variante numérica em critério monetário")
                if (m.containsKey("centavos")) return ValorLido.Incompativel("mais de uma variante preenchida")
                numeroExato(m["numero"])?.let { ValorLido.Valido(ValorCanonico.Numero(it)) } ?: ValorLido.Incompativel("número inválido")
            }
            else -> ValorLido.Incompativel("tipo de valor desconhecido")
        }
    }

    private fun numeroExato(x: Any?): Double? = when (x) {
        is Double -> x.takeIf { it.isFinite() }
        is Long -> if (x in -MAX_INTEIRO_EXATO..MAX_INTEIRO_EXATO) x.toDouble() else null
        else -> null
    }

    private fun texto(d: Map<String, Any?>, campo: String): String =
        (d[campo] as? String)?.takeIf { it.isNotBlank() } ?: throw DocumentoInvalidoException("$campo obrigatório.")

    private fun booleano(d: Map<String, Any?>, campo: String): Boolean =
        d[campo] as? Boolean ?: throw DocumentoInvalidoException("validacao.$campo deve ser explícito.")

    private fun instante(x: Any?): Instant? = when (x) {
        is Timestamp -> Instant.ofEpochSecond(x.seconds, x.nanoseconds.toLong())
        else -> null
    }
}
