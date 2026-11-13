package br.edu.ufu.pdm.mcdm.domain.usecase

import br.edu.ufu.pdm.mcdm.domain.model.Alternativa
import br.edu.ufu.pdm.mcdm.domain.model.Categoria
import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoOficial
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.model.Proveniencia
import br.edu.ufu.pdm.mcdm.domain.model.ValorAlternativa
import br.edu.ufu.pdm.mcdm.domain.model.ValorLido
import br.edu.ufu.pdm.mcdm.domain.validation.Compatibilidade
import br.edu.ufu.pdm.mcdm.domain.validation.CompatibilidadeDefinicoes
import java.util.UUID

sealed interface Elegibilidade {
    data class Elegivel(val aviso: String? = null) : Elegibilidade
    data class Bloqueada(val motivo: String) : Elegibilidade
}

/**
 * Copia um produto validado para a avaliação (snapshot independente, DEC-042).
 * Original incompatível bloqueia a seleção; efetivo não contorna esse bloqueio (DEC-041).
 */
class PrepararAlternativa(private val novoId: () -> String = { UUID.randomUUID().toString() }) {

    fun elegibilidadeOficial(produto: ProdutoOficial, categoria: Categoria?, destino: List<CriterioDefinicao>): Elegibilidade {
        if (produto.problemas.isNotEmpty()) return Elegibilidade.Bloqueada("Documento inválido: ${produto.problemas.first()}")
        if (categoria == null) return Elegibilidade.Bloqueada("Definição da categoria indisponível.")
        if (produto.revisaoDefinicao != categoria.revisaoDefinicao) {
            return Elegibilidade.Bloqueada(
                "Revisão do produto (${produto.revisaoDefinicao}) difere da categoria (${categoria.revisaoDefinicao}); definição indeterminada.",
            )
        }
        produto.valores.entries.firstOrNull { it.value is ValorLido.Incompativel }?.let { (id, v) ->
            val nome = destino.firstOrNull { it.id == id }?.nome ?: id
            return Elegibilidade.Bloqueada("$nome em formato incompatível: ${(v as ValorLido.Incompativel).motivo}. Corrigir na fonte.")
        }
        return when (val c = CompatibilidadeDefinicoes.comparar(categoria.criterios, destino)) {
            Compatibilidade.Compativel -> {
                val faltando = destino.count { produto.valores[it.id] !is ValorLido.Valido }
                Elegibilidade.Elegivel(if (faltando > 0) "$faltando valor(es) ausente(s): informe na etapa de valores." else null)
            }
            is Compatibilidade.Incompativel -> Elegibilidade.Bloqueada(c.motivos.first())
            is Compatibilidade.Indeterminada -> Elegibilidade.Bloqueada(c.motivos.first())
        }
    }

    fun elegibilidadePessoal(produto: ProdutoPessoal, destino: List<CriterioDefinicao>): Elegibilidade =
        when (val c = CompatibilidadeDefinicoes.comparar(produto.definicoes, destino)) {
            Compatibilidade.Compativel -> Elegibilidade.Elegivel()
            is Compatibilidade.Incompativel ->
                Elegibilidade.Bloqueada("${c.motivos.first()} Cadastre um novo produto com as definições atuais.")
            is Compatibilidade.Indeterminada -> Elegibilidade.Bloqueada(c.motivos.first())
        }

    fun deOficial(produto: ProdutoOficial, destino: List<CriterioDefinicao>, ordem: Int): Alternativa =
        Alternativa(
            alternativaId = novoId(),
            origemProduto = OrigemProduto.OFICIAL,
            idProdutoOrigem = produto.id,
            identificacao = produto.identificacao,
            ordem = ordem,
            valores = destino.associate { c ->
                val v = (produto.valores[c.id] as? ValorLido.Valido)?.valor
                c.id to ValorAlternativa.copiaDoOriginal(v, produto.referencias[c.id] ?: Proveniencia.DESCONHECIDA)
            },
            atualizadoNaOrigemEm = produto.atualizadoEm,
        )

    fun dePessoal(produto: ProdutoPessoal, destino: List<CriterioDefinicao>, ordem: Int): Alternativa =
        Alternativa(
            alternativaId = novoId(),
            origemProduto = OrigemProduto.PESSOAL,
            idProdutoOrigem = produto.id,
            identificacao = produto.identificacao,
            ordem = ordem,
            valores = destino.associate { c ->
                c.id to ValorAlternativa.copiaDoOriginal(produto.valores[c.id], produto.referencias[c.id] ?: Proveniencia.DESCONHECIDA)
            },
        )
}
