package br.edu.ufu.pdm.mcdm.domain.model

import java.time.Instant

/** Estado do valor original lido de uma fonte. */
sealed interface ValorLido {
    data object Ausente : ValorLido
    data class Valido(val valor: ValorCanonico) : ValorLido

    /** Representação fora do contrato (por exemplo, dinheiro em Double). Bloqueia seleção. */
    data class Incompativel(val motivo: String) : ValorLido
}

data class IdentificacaoProduto(
    val nome: String,
    val fabricante: String? = null,
    val modelo: String? = null,
    val versao: String? = null,
    val ano: Int? = null,
) {
    /** Linha secundária com o que é conhecido; ausência não vira texto fictício. */
    val complemento: String
        get() = listOfNotNull(fabricante, modelo, versao, ano?.toString()).joinToString(" · ")
}

data class ProdutoOficial(
    val id: String,
    val categoriaId: String,
    val revisaoDefinicao: String,
    val identificacao: IdentificacaoProduto,
    val valores: Map<String, ValorLido>,
    val referencias: Map<String, Proveniencia>,
    val atualizadoEm: Instant?,
    /** Problemas do documento (critério desconhecido, campos contraditórios...). */
    val problemas: List<String> = emptyList(),
)

/** Produto pessoal: guarda a cópia das definições sob as quais os valores foram informados (DEC-042). */
data class ProdutoPessoal(
    val id: String,
    val categoriaId: String,
    val categoriaNome: String,
    val revisaoDefinicao: String?,
    val identificacao: IdentificacaoProduto,
    val definicoes: List<CriterioDefinicao>,
    val valores: Map<String, ValorCanonico>,
    val referencias: Map<String, Proveniencia> = emptyMap(),
)

/** Origem do evento do catálogo oficial (DEC-020). */
enum class OrigemDados { SERVIDOR, CACHE }

sealed interface EstadoConsulta<out T> {
    data object Carregando : EstadoConsulta<Nothing>

    /**
     * Dados recebidos. [confirmadoNoServidorEm] é o instante local da última leitura confirmada
     * pelo servidor nesta execução; leitura do cache não o atualiza.
     */
    data class Dados<T>(
        val dados: T,
        val origem: OrigemDados,
        val confirmadoNoServidorEm: Instant?,
        /** Documentos fora do contrato que não puderam ser interpretados. */
        val documentosInvalidos: Int = 0,
    ) : EstadoConsulta<T>

    data object PermissaoNegada : EstadoConsulta<Nothing>
    data class Indisponivel(val mensagem: String) : EstadoConsulta<Nothing>
}
