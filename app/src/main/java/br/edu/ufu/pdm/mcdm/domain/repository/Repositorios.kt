package br.edu.ufu.pdm.mcdm.domain.repository

import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.Categoria
import br.edu.ufu.pdm.mcdm.domain.model.EstadoConsulta
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoOficial
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.model.ResumoAvaliacao
import kotlinx.coroutines.flow.Flow

/** Catálogo oficial remoto, somente leitura (DEC-009/040). */
interface CatalogoRepository {
    fun observarCategorias(): Flow<EstadoConsulta<List<Categoria>>>
    fun observarProdutos(categoriaId: String): Flow<EstadoConsulta<List<ProdutoOficial>>>
}

/** Catálogo pessoal local (DEC-007/012). */
interface ProdutoPessoalRepository {
    fun observarPorCategoria(categoriaId: String): Flow<List<ProdutoPessoal>>
    fun observar(id: String): Flow<ProdutoPessoal?>
    suspend fun obter(id: String): ProdutoPessoal?
    suspend fun salvar(produto: ProdutoPessoal)
    suspend fun excluir(id: String)
}

/** Erro de leitura de um agregado salvo que viola o contrato: não é apresentado como íntegro. */
class AvaliacaoCorrompidaException(val avaliacaoId: String, val problemas: List<String>) :
    Exception("Avaliação $avaliacaoId inconsistente: ${problemas.joinToString("; ")}")

/** Avaliações locais (DEC-004/038). Salvar e excluir são transacionais. */
interface AvaliacaoRepository {
    fun observarResumos(): Flow<List<ResumoAvaliacao>>

    /** Lê sob leitura consistente e valida invariantes; lança [AvaliacaoCorrompidaException]. */
    suspend fun obter(id: String): Avaliacao?
    suspend fun salvar(avaliacao: Avaliacao)
    suspend fun excluir(id: String)
}
