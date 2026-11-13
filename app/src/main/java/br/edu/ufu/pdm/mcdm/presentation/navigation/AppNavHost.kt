package br.edu.ufu.pdm.mcdm.presentation.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.edu.ufu.pdm.mcdm.di.AppContainer
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.AvaliacaoViewModel
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.EventoSessao
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.Passo
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.TelaAhp
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.TelaDetalhesAvaliacao
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.TelaPesos
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.TelaResultado
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.TelaRevisao
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.TelaSelecao
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.TelaSessao
import br.edu.ufu.pdm.mcdm.presentation.avaliacao.TelaValores
import br.edu.ufu.pdm.mcdm.presentation.avaliacoes.ListaAvaliacoesViewModel
import br.edu.ufu.pdm.mcdm.presentation.avaliacoes.TelaListaAvaliacoes
import br.edu.ufu.pdm.mcdm.presentation.catalogo.CategoriasViewModel
import br.edu.ufu.pdm.mcdm.presentation.catalogo.DetalheProdutoViewModel
import br.edu.ufu.pdm.mcdm.presentation.catalogo.ProdutosViewModel
import br.edu.ufu.pdm.mcdm.presentation.catalogo.TelaCategorias
import br.edu.ufu.pdm.mcdm.presentation.catalogo.TelaDetalheProduto
import br.edu.ufu.pdm.mcdm.presentation.catalogo.TelaProdutos
import br.edu.ufu.pdm.mcdm.presentation.exportacao.compartilharRelatorio
import br.edu.ufu.pdm.mcdm.presentation.produtopessoal.FormularioPessoalViewModel
import br.edu.ufu.pdm.mcdm.presentation.produtopessoal.TelaFormularioPessoal

/** Cria (ou recupera) um ViewModel no [dono] com injeção manual pelo contêiner (DEC-044). */
@Composable
private inline fun <reified VM : ViewModel> vm(dono: ViewModelStoreOwner, noinline criar: () -> VM): VM =
    viewModel(viewModelStoreOwner = dono, factory = viewModelFactory { initializer { criar() } })

private data class Aba(val rotulo: String, val rota: Any, val icone: androidx.compose.ui.graphics.vector.ImageVector)

private val abas = listOf(
    Aba("Catálogo", RotaCatalogo, Icons.Outlined.Category),
    Aba("Avaliações", RotaAvaliacoes, Icons.Outlined.Assessment),
)

private fun Passo.rota(): Any = when (this) {
    Passo.SELECAO -> PassoSelecao
    Passo.VALORES -> PassoValores
    Passo.AHP -> PassoAhp
    Passo.PESOS -> PassoPesos
    Passo.REVISAO -> PassoRevisao
    Passo.RESULTADO -> PassoResultado
}

/** Vai a um passo da sessão; se ele já está na pilha, retorna a ele (evita ciclos). */
private fun NavHostController.irParaPasso(passo: Passo, substituirInicio: Boolean = false) {
    val rota = passo.rota()
    if (substituirInicio) {
        navigate(rota) { popUpTo<PassoCarregar> { inclusive = true } }
        return
    }
    if (!popBackStack(rota, inclusive = false)) navigate(rota) { launchSingleTop = true }
}

@Composable
fun AppNavHost(container: AppContainer) {
    val nav = rememberNavController()
    val entradaAtual by nav.currentBackStackEntryAsState()
    val destino = entradaAtual?.destination
    val mostrarAbas = destino?.let { d -> abas.any { d.hasRoute(it.rota::class) } } ?: true

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (mostrarAbas) {
                NavigationBar {
                    abas.forEach { aba ->
                        NavigationBarItem(
                            selected = destino?.hierarchy?.any { it.hasRoute(aba.rota::class) } == true,
                            onClick = {
                                nav.navigate(aba.rota) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(aba.icone, contentDescription = null) },
                            label = { Text(aba.rotulo) },
                        )
                    }
                }
            }
        },
    ) { margens ->
        NavHost(
            navController = nav,
            startDestination = RotaCatalogo,
            modifier = Modifier.padding(margens).consumeWindowInsets(margens),
        ) {
            composable<RotaCatalogo> { e ->
                TelaCategorias(
                    vm = vm(e) { CategoriasViewModel(container.catalogo) },
                    onConsultar = { nav.navigate(RotaProdutos(it)) },
                    onNovaAvaliacao = { nav.navigate(RotaAvaliacao(categoriaId = it)) },
                )
            }
            composable<RotaAvaliacoes> { e ->
                TelaListaAvaliacoes(
                    vm = vm(e) { ListaAvaliacoesViewModel(container.avaliacoes, container.catalogo) },
                    onAbrir = { nav.navigate(RotaAvaliacao(avaliacaoId = it)) },
                    onNova = { nav.navigate(RotaAvaliacao(categoriaId = it)) },
                )
            }
            composable<RotaProdutos> { e ->
                val r = e.toRoute<RotaProdutos>()
                TelaProdutos(
                    vm = vm(e) { ProdutosViewModel(r.categoriaId, container.catalogo, container.produtosPessoais) },
                    onVoltar = { nav.popBackStack() },
                    onDetalhe = { origem, id -> nav.navigate(RotaDetalheProduto(r.categoriaId, origem.name, id)) },
                    onCadastrar = { nav.navigate(RotaFormularioPessoal(r.categoriaId)) },
                    onIniciarAvaliacao = { nav.navigate(RotaAvaliacao(categoriaId = r.categoriaId)) },
                )
            }
            composable<RotaDetalheProduto> { e ->
                val r = e.toRoute<RotaDetalheProduto>()
                TelaDetalheProduto(
                    vm = vm(e) { DetalheProdutoViewModel(r.categoriaId, r.origem, r.produtoId, container.catalogo, container.produtosPessoais) },
                    onVoltar = { nav.popBackStack() },
                    onEditar = {
                        if (r.origem == OrigemProduto.PESSOAL.name) nav.navigate(RotaFormularioPessoal(r.categoriaId, r.produtoId))
                    },
                )
            }
            composable<RotaFormularioPessoal> { e ->
                val r = e.toRoute<RotaFormularioPessoal>()
                TelaFormularioPessoal(
                    vm = vm(e) {
                        FormularioPessoalViewModel(r.categoriaId, r.produtoId, container.catalogo, container.produtosPessoais, container.salvarProdutoPessoal)
                    },
                    onSair = { nav.popBackStack() },
                )
            }
            sessaoAvaliacao(nav, container)
        }
    }
}

/** Grafo aninhado da sessão: todos os passos compartilham o AvaliacaoViewModel do grafo (DEC-045). */
private fun NavGraphBuilder.sessaoAvaliacao(nav: NavHostController, container: AppContainer) {
    navigation<RotaAvaliacao>(startDestination = PassoCarregar) {
        passo<PassoCarregar>(nav, container) { vm, voltar ->
            TelaSessao(vm, Passo.SELECAO, "Avaliação", voltar) {}
        }
        passo<PassoSelecao>(nav, container) { vm, voltar ->
            TelaSelecao(
                vm, voltar,
                onCadastrarPessoal = { vm.estado.value.edicao?.conteudo?.categoriaId?.let { nav.navigate(RotaFormularioPessoal(it)) } },
                onContinuar = { nav.irParaPasso(Passo.VALORES) },
            )
        }
        passo<PassoValores>(nav, container) { vm, voltar -> TelaValores(vm, voltar) }
        passo<PassoAhp>(nav, container) { vm, voltar -> TelaAhp(vm, voltar) }
        passo<PassoPesos>(nav, container) { vm, voltar -> TelaPesos(vm, voltar, onRevisar = { nav.irParaPasso(Passo.REVISAO) }) }
        passo<PassoRevisao>(nav, container) { vm, voltar ->
            TelaRevisao(
                vm, voltar,
                onEditarValores = { nav.irParaPasso(Passo.VALORES) },
                onEditarAhp = { nav.irParaPasso(Passo.AHP) },
            )
        }
        passo<PassoResultado>(nav, container) { vm, voltar ->
            TelaResultado(
                vm, voltar,
                onDetalhes = { nav.navigate(PassoDetalhes) },
                onRevisao = { nav.irParaPasso(Passo.REVISAO) },
                onEditarValores = { nav.irParaPasso(Passo.VALORES) },
            )
        }
        passo<PassoDetalhes>(nav, container) { vm, _ -> TelaDetalhesAvaliacao(vm) { nav.popBackStack() } }
    }
}

private inline fun <reified T : Any> NavGraphBuilder.passo(
    nav: NavHostController,
    container: AppContainer,
    crossinline conteudo: @Composable (AvaliacaoViewModel, () -> Unit) -> Unit,
) {
    composable<T> { entrada ->
        val vm = vmSessao(nav, entrada, container)
        TratarEventos(vm, nav, entrada)
        // Voltar no primeiro passo da sessão encerra a sessão (aplica D01); nos demais, volta um passo.
        val voltar: () -> Unit = {
            val anterior = nav.previousBackStackEntry?.destination
            if (anterior != null && anterior.parent?.hasRoute(RotaAvaliacao::class) == true && !anterior.hasRoute(PassoCarregar::class)) {
                nav.popBackStack()
            } else {
                vm.pedirSaida()
            }
        }
        conteudo(vm, voltar)
    }
}

@Composable
private fun vmSessao(nav: NavHostController, entrada: NavBackStackEntry, container: AppContainer): AvaliacaoViewModel {
    val grafo = remember(entrada) { nav.getBackStackEntry<RotaAvaliacao>() }
    val r = grafo.toRoute<RotaAvaliacao>()
    return vm(grafo) {
        AvaliacaoViewModel(
            avaliacaoId = r.avaliacaoId,
            categoriaId = r.categoriaId,
            catalogo = container.catalogo,
            pessoais = container.produtosPessoais,
            avaliacoes = container.avaliacoes,
            calcularAhp = container.calcularAhp,
            processarAvaliacao = container.processarAvaliacao,
            salvarAvaliacao = container.salvarAvaliacao,
            exportarAvaliacao = container.exportarAvaliacao,
            preparar = container.prepararAlternativa,
            relogio = container.relogio,
        )
    }
}

/** Eventos de navegação da sessão: tratados só pela entrada no topo da pilha (uma vez cada). */
@Composable
private fun TratarEventos(vm: AvaliacaoViewModel, nav: NavHostController, entrada: NavBackStackEntry) {
    val context = LocalContext.current
    val eventos by vm.eventos.collectAsStateWithLifecycle()
    val topo by nav.currentBackStackEntryAsState()
    LaunchedEffect(eventos, topo) {
        if (topo?.id != entrada.id) return@LaunchedEffect
        val ev = eventos.firstOrNull { it !is EventoSessao.FocarValor } ?: return@LaunchedEffect
        vm.consumir(ev)
        when (ev) {
            is EventoSessao.Navegar -> nav.irParaPasso(ev.passo, ev.substituirInicio)
            EventoSessao.Sair -> nav.popBackStack<RotaAvaliacao>(inclusive = true)
            is EventoSessao.Compartilhar -> if (!compartilharRelatorio(context, ev.relatorio)) {
                Toast.makeText(context, "Nenhum app disponível para receber o PDF.", Toast.LENGTH_LONG).show()
            }
            is EventoSessao.FocarValor -> Unit
        }
    }
}
