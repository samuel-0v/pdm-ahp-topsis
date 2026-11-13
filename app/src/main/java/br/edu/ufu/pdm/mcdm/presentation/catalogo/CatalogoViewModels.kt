package br.edu.ufu.pdm.mcdm.presentation.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufu.pdm.mcdm.domain.model.Categoria
import br.edu.ufu.pdm.mcdm.domain.model.EstadoConsulta
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoOficial
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.model.ValorLido
import br.edu.ufu.pdm.mcdm.domain.repository.CatalogoRepository
import br.edu.ufu.pdm.mcdm.domain.repository.ProdutoPessoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriasViewModel(catalogo: CatalogoRepository) : ViewModel() {
    val estado: StateFlow<EstadoConsulta<List<Categoria>>> = catalogo.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoConsulta.Carregando)
}

data class ProdutosUiState(
    val categoria: Categoria? = null,
    val oficiais: EstadoConsulta<List<ProdutoOficial>> = EstadoConsulta.Carregando,
    val pessoais: List<ProdutoPessoal>? = null,
    val erroPessoais: String? = null,
)

/** Produto oficial é incompleto quando falta valor de algum critério da categoria. */
fun ProdutoOficial.incompleto(categoria: Categoria?): Boolean =
    categoria?.criterios?.any { valores[it.id] !is ValorLido.Valido } ?: valores.values.any { it !is ValorLido.Valido }

class ProdutosViewModel(
    categoriaId: String,
    catalogo: CatalogoRepository,
    pessoais: ProdutoPessoalRepository,
) : ViewModel() {
    private val erroPessoais = MutableStateFlow<String?>(null)

    val estado: StateFlow<ProdutosUiState> = combine(
        catalogo.observarCategorias(),
        catalogo.observarProdutos(categoriaId),
        pessoais.observarPorCategoria(categoriaId).map<List<ProdutoPessoal>, List<ProdutoPessoal>?> { it }
            .catch { erroPessoais.value = "Falha ao ler os produtos pessoais."; emit(null) },
        erroPessoais,
    ) { cats, oficiais, lista, erro ->
        ProdutosUiState(
            categoria = (cats as? EstadoConsulta.Dados)?.dados?.firstOrNull { it.id == categoriaId },
            oficiais = oficiais,
            pessoais = lista,
            erroPessoais = erro,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProdutosUiState())
}

sealed interface DetalheUiState {
    data object Carregando : DetalheUiState
    data class Oficial(val produto: ProdutoOficial, val categoria: Categoria?, val origemConsulta: EstadoConsulta.Dados<*>) : DetalheUiState
    data class Pessoal(val produto: ProdutoPessoal) : DetalheUiState
    data class Indisponivel(val mensagem: String) : DetalheUiState
}

class DetalheProdutoViewModel(
    private val categoriaId: String,
    origem: String,
    private val produtoId: String,
    catalogo: CatalogoRepository,
    private val pessoais: ProdutoPessoalRepository,
) : ViewModel() {
    private val _excluido = MutableStateFlow(false)
    val excluido = _excluido.asStateFlow()
    private val _erroExclusao = MutableStateFlow<String?>(null)
    val erroExclusao = _erroExclusao.asStateFlow()

    val estado: StateFlow<DetalheUiState> = if (origem == "OFICIAL") {
        combine(catalogo.observarCategorias(), catalogo.observarProdutos(categoriaId)) { cats, prods ->
            when (prods) {
                EstadoConsulta.Carregando -> DetalheUiState.Carregando
                is EstadoConsulta.Dados -> prods.dados.firstOrNull { it.id == produtoId }
                    ?.let { DetalheUiState.Oficial(it, (cats as? EstadoConsulta.Dados)?.dados?.firstOrNull { c -> c.id == categoriaId }, prods) }
                    ?: DetalheUiState.Indisponivel("Produto não encontrado nos dados disponíveis.")
                EstadoConsulta.PermissaoNegada -> DetalheUiState.Indisponivel("Acesso ao catálogo negado.")
                is EstadoConsulta.Indisponivel -> DetalheUiState.Indisponivel(prods.mensagem)
            }
        }
    } else {
        pessoais.observar(produtoId).map { p -> p?.let { DetalheUiState.Pessoal(it) } ?: DetalheUiState.Indisponivel("Produto pessoal não encontrado.") }
            .catch { emit(DetalheUiState.Indisponivel("Falha ao ler o produto pessoal.")) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetalheUiState.Carregando)

    /** Exclusão confirmada; não altera avaliações (snapshots independentes). */
    fun excluir() {
        viewModelScope.launch {
            try {
                pessoais.excluir(produtoId)
                _erroExclusao.value = null
                _excluido.value = true
            } catch (e: Exception) {
                _erroExclusao.value = "Falha ao excluir. O produto foi mantido."
            }
        }
    }
}
