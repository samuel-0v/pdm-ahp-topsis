package br.edu.ufu.pdm.mcdm.presentation.avaliacoes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import br.edu.ufu.pdm.mcdm.domain.model.Categoria
import br.edu.ufu.pdm.mcdm.domain.model.EstadoConsulta
import br.edu.ufu.pdm.mcdm.domain.model.EstadoSalvo
import br.edu.ufu.pdm.mcdm.domain.model.ResumoAvaliacao
import br.edu.ufu.pdm.mcdm.domain.repository.AvaliacaoRepository
import br.edu.ufu.pdm.mcdm.domain.repository.CatalogoRepository
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoPrincipal
import br.edu.ufu.pdm.mcdm.presentation.components.Carregando
import br.edu.ufu.pdm.mcdm.presentation.components.Cartao
import br.edu.ufu.pdm.mcdm.presentation.components.DialogoExclusao
import br.edu.ufu.pdm.mcdm.presentation.components.Mensagem
import br.edu.ufu.pdm.mcdm.presentation.components.Selo
import br.edu.ufu.pdm.mcdm.presentation.components.TelaBase
import br.edu.ufu.pdm.mcdm.presentation.components.Tom
import br.edu.ufu.pdm.mcdm.presentation.components.formatarInstante
import br.edu.ufu.pdm.mcdm.presentation.theme.Cores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ListaUiState {
    data object Carregando : ListaUiState
    data class Itens(val itens: List<ResumoAvaliacao>) : ListaUiState
    /** Falha de leitura não é lista vazia. */
    data object Falha : ListaUiState
}

class ListaAvaliacoesViewModel(private val repo: AvaliacaoRepository, catalogo: CatalogoRepository) : ViewModel() {
    val estado: StateFlow<ListaUiState> = repo.observarResumos()
        .map<List<ResumoAvaliacao>, ListaUiState> { ListaUiState.Itens(it) }
        .catch { emit(ListaUiState.Falha) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ListaUiState.Carregando)

    val categorias: StateFlow<EstadoConsulta<List<Categoria>>> = catalogo.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoConsulta.Carregando)

    private val _mensagem = MutableStateFlow<Pair<String, Tom>?>(null)
    val mensagem = _mensagem.asStateFlow()

    fun excluir(id: String) {
        viewModelScope.launch {
            _mensagem.value = try {
                repo.excluir(id)
                "Avaliação excluída deste dispositivo." to Tom.SUCESSO
            } catch (e: Exception) {
                "Falha ao excluir. A avaliação foi mantida." to Tom.ERRO
            }
        }
    }
}

fun rotuloEstado(e: EstadoSalvo) = when (e) {
    EstadoSalvo.RASCUNHO -> "Rascunho"
    EstadoSalvo.CONCLUIDA_RANKING -> "Concluída com ranking"
    EstadoSalvo.CONCLUIDA_SEM_DIFERENCIACAO -> "Concluída — sem diferenciação"
}

@Composable
fun TelaListaAvaliacoes(vm: ListaAvaliacoesViewModel, onAbrir: (String) -> Unit, onNova: (String) -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val mensagem by vm.mensagem.collectAsStateWithLifecycle()
    var excluir by rememberSaveable { mutableStateOf<String?>(null) }
    var escolherCategoria by rememberSaveable { mutableStateOf(false) }

    TelaBase(
        titulo = "Avaliações salvas",
        onVoltar = null,
        rodape = { BotaoPrincipal("Nova avaliação", { escolherCategoria = true }) },
    ) {
        Mensagem("Avaliações ficam somente neste dispositivo e podem ser perdidas ao desinstalar o aplicativo.")
        mensagem?.let { Mensagem(it.first, it.second) }
        when (val e = estado) {
            ListaUiState.Carregando -> Carregando()
            ListaUiState.Falha -> Mensagem("Falha ao ler as avaliações salvas.", Tom.ERRO)
            is ListaUiState.Itens -> if (e.itens.isEmpty()) {
                Text("Nenhuma avaliação salva ainda.", style = MaterialTheme.typography.bodyLarge)
            } else {
                e.itens.forEach { r ->
                    Cartao(onClick = { onAbrir(r.id) }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(r.rotulo, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                            Selo(
                                rotuloEstado(r.estado),
                                if (r.estado == EstadoSalvo.RASCUNHO) Cores.avisoSuave else Cores.sucessoSuave,
                                if (r.estado == EstadoSalvo.RASCUNHO) Cores.aviso else Cores.sucesso,
                            )
                        }
                        Text("${r.categoriaNome} · ${r.quantidadeAlternativas} alternativa(s)", style = MaterialTheme.typography.bodySmall)
                        Text("Salva em ${formatarInstante(r.salvaEm)}", style = MaterialTheme.typography.bodySmall)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { onAbrir(r.id) }) { Text(if (r.estado == EstadoSalvo.RASCUNHO) "Retomar" else "Consultar") }
                            TextButton(onClick = { excluir = r.id }) { Text("Excluir", color = Cores.erro) }
                        }
                    }
                }
            }
        }
    }

    excluir?.let { id ->
        DialogoExclusao(
            objeto = "avaliação",
            consequencia = "A avaliação e seus resultados serão removidos permanentemente deste dispositivo. Os produtos dos catálogos não são afetados.",
            onConfirmar = { excluir = null; vm.excluir(id) },
            onCancelar = { excluir = null },
        )
    }

    if (escolherCategoria) {
        val cats by vm.categorias.collectAsStateWithLifecycle()
        AlertDialog(
            onDismissRequest = { escolherCategoria = false },
            title = { Text("Categoria da avaliação") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("A categoria não pode ser alterada depois.", style = MaterialTheme.typography.bodySmall)
                    when (val c = cats) {
                        is EstadoConsulta.Dados -> if (c.dados.isEmpty()) {
                            Mensagem("Nenhuma categoria disponível offline. Conecte-se para consultar.", Tom.AVISO)
                        } else {
                            c.dados.forEach { cat ->
                                TextButton(onClick = { escolherCategoria = false; onNova(cat.id) }, modifier = Modifier.fillMaxWidth()) { Text(cat.nome) }
                            }
                        }
                        EstadoConsulta.Carregando -> Carregando()
                        else -> Mensagem("Categorias indisponíveis no momento.", Tom.ERRO)
                    }
                }
            },
            confirmButton = { TextButton(onClick = { escolherCategoria = false }) { Text("Cancelar") } },
        )
    }
}
