package br.edu.ufu.pdm.mcdm.domain.usecase

import br.edu.ufu.pdm.mcdm.domain.exportacao.ConteudoRelatorio
import br.edu.ufu.pdm.mcdm.domain.exportacao.GeradorRelatorioPdf
import br.edu.ufu.pdm.mcdm.domain.exportacao.RelatorioGerado
import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.EdicaoAvaliacao
import br.edu.ufu.pdm.mcdm.domain.model.EstadoSalvo
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.repository.AvaliacaoRepository
import br.edu.ufu.pdm.mcdm.domain.repository.ProdutoPessoalRepository
import br.edu.ufu.pdm.mcdm.domain.validation.ValidadorAgregado
import br.edu.ufu.pdm.mcdm.domain.validation.ValidadorValor
import java.time.Clock
import java.time.Instant

sealed interface ResultadoOperacao<out T> {
    data class Sucesso<T>(val valor: T) : ResultadoOperacao<T>
    data class Erro(val mensagem: String) : ResultadoOperacao<Nothing>
}

/**
 * Salvamento manual e atômico (DEC-025, RNF05). Grava o conteúdo salvável da edição:
 * rascunho quando não há TOPSIS vigente; conclusão correspondente quando há.
 * Falha não altera a versão salva e preserva a edição (o chamador mantém o estado).
 */
class SalvarAvaliacao(private val repositorio: AvaliacaoRepository, private val relogio: Clock) {
    suspend operator fun invoke(edicao: EdicaoAvaliacao): ResultadoOperacao<Avaliacao> {
        val conteudo = edicao.conteudoSalvavel().copy(salvaEm = Instant.now(relogio))
        val problemas = ValidadorAgregado.problemas(conteudo)
        if (problemas.isNotEmpty()) return ResultadoOperacao.Erro("Não foi possível salvar: ${problemas.first()}")
        return try {
            repositorio.salvar(conteudo)
            ResultadoOperacao.Sucesso(conteudo)
        } catch (e: Exception) {
            ResultadoOperacao.Erro("Falha ao salvar neste dispositivo. A edição foi preservada.")
        }
    }
}

/** Cadastro/edição pessoal: não admite campo obrigatório inválido (RF06). Categoria imutável (DEC-021). */
class SalvarProdutoPessoal(private val repositorio: ProdutoPessoalRepository) {
    suspend operator fun invoke(produto: ProdutoPessoal): ResultadoOperacao<ProdutoPessoal> {
        if (produto.identificacao.nome.isBlank()) return ResultadoOperacao.Erro("Informe o nome do produto.")
        for (c in produto.definicoes) {
            val v = produto.valores[c.id]
            if (v == null) {
                if (c.validacao.obrigatorioCadastro) return ResultadoOperacao.Erro("${c.nome}: valor obrigatório.")
                continue
            }
            ValidadorValor.validar(v, c.validacao)?.let { return ResultadoOperacao.Erro("${c.nome}: $it") }
        }
        val existente = repositorio.obter(produto.id)
        if (existente != null && existente.categoriaId != produto.categoriaId) {
            return ResultadoOperacao.Erro("A categoria de um produto pessoal não pode ser alterada.")
        }
        return try {
            repositorio.salvar(produto)
            ResultadoOperacao.Sucesso(produto)
        } catch (e: Exception) {
            ResultadoOperacao.Erro("Falha ao salvar o produto. O formulário foi preservado.")
        }
    }
}

/**
 * Exportação PDF da versão SALVA e processada (DEC-032). Não executa o motor nem consulta catálogo.
 * Rascunho e resultado desatualizado não são exportáveis.
 */
class ExportarAvaliacao(
    private val repositorio: AvaliacaoRepository,
    private val gerador: GeradorRelatorioPdf,
    private val relogio: Clock,
) {
    suspend operator fun invoke(avaliacaoId: String): ResultadoOperacao<RelatorioGerado> {
        val salva = try {
            repositorio.obter(avaliacaoId)
        } catch (e: Exception) {
            return ResultadoOperacao.Erro("A versão salva não pôde ser lida de forma íntegra.")
        } ?: return ResultadoOperacao.Erro("Avaliação ainda não salva.")
        if (salva.estado == EstadoSalvo.RASCUNHO) return ResultadoOperacao.Erro("Rascunhos não são exportáveis.")
        val problemas = ValidadorAgregado.problemas(salva)
        if (problemas.isNotEmpty()) return ResultadoOperacao.Erro("Versão salva inconsistente: ${problemas.first()}")
        return try {
            ResultadoOperacao.Sucesso(gerador.gerar(ConteudoRelatorio.de(salva, Instant.now(relogio))))
        } catch (e: Exception) {
            ResultadoOperacao.Erro("Falha ao gerar o PDF. A avaliação salva não foi alterada.")
        }
    }
}
