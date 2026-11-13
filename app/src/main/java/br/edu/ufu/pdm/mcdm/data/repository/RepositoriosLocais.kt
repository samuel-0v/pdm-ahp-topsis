package br.edu.ufu.pdm.mcdm.data.repository

import br.edu.ufu.pdm.mcdm.data.local.AvaliacaoDao
import br.edu.ufu.pdm.mcdm.data.local.Codecs
import br.edu.ufu.pdm.mcdm.data.local.ProdutoPessoalDao
import br.edu.ufu.pdm.mcdm.data.local.RepresentacaoInvalidaException
import br.edu.ufu.pdm.mcdm.data.mapper.AvaliacaoMapper
import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.EstadoSalvo
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.model.ResumoAvaliacao
import br.edu.ufu.pdm.mcdm.domain.repository.AvaliacaoCorrompidaException
import br.edu.ufu.pdm.mcdm.domain.repository.AvaliacaoRepository
import br.edu.ufu.pdm.mcdm.domain.repository.ProdutoPessoalRepository
import br.edu.ufu.pdm.mcdm.domain.validation.ValidadorAgregado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class ProdutoPessoalRepositoryRoom(private val dao: ProdutoPessoalDao) : ProdutoPessoalRepository {
    /** Registros ilegíveis são omitidos da lista em vez de exibidos como íntegros. */
    override fun observarPorCategoria(categoriaId: String): Flow<List<ProdutoPessoal>> =
        dao.observarPorCategoria(categoriaId).map { l -> l.mapNotNull { runCatching { AvaliacaoMapper.produtoDeEntidade(it) }.getOrNull() } }

    override fun observar(id: String): Flow<ProdutoPessoal?> = dao.observar(id).map { it?.let(AvaliacaoMapper::produtoDeEntidade) }

    override suspend fun obter(id: String): ProdutoPessoal? = dao.obter(id)?.let(AvaliacaoMapper::produtoDeEntidade)

    override suspend fun salvar(produto: ProdutoPessoal) = dao.salvar(AvaliacaoMapper.produtoParaEntidade(produto))

    /** Não toca avaliações: elas guardam snapshots independentes. */
    override suspend fun excluir(id: String) {
        dao.excluir(id)
    }
}

class AvaliacaoRepositoryRoom(private val dao: AvaliacaoDao) : AvaliacaoRepository {
    override fun observarResumos(): Flow<List<ResumoAvaliacao>> = dao.observarResumos().map { l ->
        l.map {
            ResumoAvaliacao(
                id = it.avaliacaoId,
                rotulo = it.titulo?.takeIf(String::isNotBlank) ?: it.categoriaNome,
                categoriaNome = it.categoriaNome,
                estado = Codecs.enumOuErro<EstadoSalvo>(it.estadoSalvo),
                quantidadeAlternativas = it.quantidade,
                salvaEm = Instant.ofEpochMilli(it.salvaEm),
            )
        }
    }

    override suspend fun obter(id: String): Avaliacao? {
        val linhas = dao.carregar(id) ?: return null
        val a = try {
            AvaliacaoMapper.deLinhas(linhas)
        } catch (e: RepresentacaoInvalidaException) {
            throw AvaliacaoCorrompidaException(id, listOf(e.message ?: "representação inválida"))
        }
        val problemas = ValidadorAgregado.problemas(a)
        if (problemas.isNotEmpty()) throw AvaliacaoCorrompidaException(id, problemas)
        return a
    }

    override suspend fun salvar(avaliacao: Avaliacao) {
        AvaliacaoMapper.validarUuid(avaliacao.id)
        avaliacao.alternativas.forEach { AvaliacaoMapper.validarUuid(it.alternativaId) }
        dao.salvar(AvaliacaoMapper.paraLinhas(avaliacao))
    }

    override suspend fun excluir(id: String) {
        dao.excluir(id)
    }
}
