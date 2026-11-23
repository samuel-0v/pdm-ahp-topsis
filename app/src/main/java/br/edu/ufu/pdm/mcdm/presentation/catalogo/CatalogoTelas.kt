package br.edu.ufu.pdm.mcdm.presentation.catalogo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.edu.ufu.pdm.mcdm.domain.model.Categoria
import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.EstadoConsulta
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.domain.model.Proveniencia
import br.edu.ufu.pdm.mcdm.domain.model.Unidades
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import br.edu.ufu.pdm.mcdm.domain.model.ValorLido
import br.edu.ufu.pdm.mcdm.domain.validation.Formatos
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoPrincipal
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoSecundario
import br.edu.ufu.pdm.mcdm.presentation.components.Carregando
import br.edu.ufu.pdm.mcdm.presentation.components.Cartao
import br.edu.ufu.pdm.mcdm.presentation.components.DialogoExclusao
import br.edu.ufu.pdm.mcdm.presentation.components.FaixaOrigemCatalogo
import br.edu.ufu.pdm.mcdm.presentation.components.Mensagem
import br.edu.ufu.pdm.mcdm.presentation.components.Selo
import br.edu.ufu.pdm.mcdm.presentation.components.SeloOrigem
import br.edu.ufu.pdm.mcdm.presentation.components.TelaBase
import br.edu.ufu.pdm.mcdm.presentation.components.Tom
import br.edu.ufu.pdm.mcdm.presentation.components.formatarInstante
import br.edu.ufu.pdm.mcdm.presentation.theme.Cores

// ---------------- T01 — Início / categorias ----------------

@Composable
fun TelaCategorias(
    vm: CategoriasViewModel,
    onConsultar: (String) -> Unit,
    onNovaAvaliacao: (String) -> Unit,
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    TelaBase(titulo = "PDM AHP-TOPSIS", subtitulo = "Catálogo", onVoltar = null) {
        Text("Escolha uma categoria", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Consulte produtos oficiais e pessoais ou crie uma avaliação comparando pelo menos duas alternativas.",
            style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        val atualizado = (estado as? EstadoConsulta.Dados)?.dados?.mapNotNull { it.atualizadoEm }?.maxOrNull()
        FaixaOrigemCatalogo(estado, atualizado)
        when (val e = estado) {
            is EstadoConsulta.Dados -> e.dados.forEach { categoria ->
                CartaoCategoria(categoria, { onConsultar(categoria.id) }, { onNovaAvaliacao(categoria.id) })
            }
            EstadoConsulta.Carregando -> Carregando("Consultando categorias…")
            else -> Unit
        }
    }
}

@Composable
private fun CartaoCategoria(c: Categoria, onConsultar: () -> Unit, onNova: () -> Unit) {
    Cartao(onClick = onConsultar) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (c.id.contains("bev") || c.id.contains("veic")) Icons.Outlined.DirectionsCar else Icons.Outlined.PhoneAndroid,
                contentDescription = null, tint = Cores.primaria,
            )
            Spacer(Modifier.width(12.dp))
            Text(c.nome, style = MaterialTheme.typography.titleMedium)
        }
        c.descricao?.let { Text(it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp)) }
        Text(
            c.criteriosOrdenados.joinToString(" · ") { it.nome },
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onConsultar) { Text("Ver produtos") }
            TextButton(onClick = onNova) { Text("Nova avaliação") }
        }
    }
}

// ---------------- T02 — Produtos da categoria ----------------

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TelaProdutos(
    vm: ProdutosViewModel,
    onVoltar: () -> Unit,
    onDetalhe: (OrigemProduto, String) -> Unit,
    onCadastrar: () -> Unit,
    onIniciarAvaliacao: () -> Unit,
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    var aba by rememberSaveable { mutableStateOf(0) }
    val categoria = estado.categoria
    TelaBase(
        titulo = categoria?.nome ?: "Produtos",
        onVoltar = onVoltar,
        rodape = { BotaoPrincipal("Iniciar avaliação", onIniciarAvaliacao, habilitado = categoria != null) },
    ) {
        PrimaryTabRow(selectedTabIndex = aba, containerColor = MaterialTheme.colorScheme.background) {
            Tab(aba == 0, { aba = 0 }, text = { Text("Oficiais") })
            Tab(aba == 1, { aba = 1 }, text = { Text("Pessoais") })
        }
        if (aba == 0) {
            FaixaOrigemCatalogo(estado.oficiais, categoria?.atualizadoEm)
            when (val o = estado.oficiais) {
                EstadoConsulta.Carregando -> Carregando()
                is EstadoConsulta.Dados -> o.dados.forEach { p ->
                    Cartao(onClick = { onDetalhe(OrigemProduto.OFICIAL, p.id) }) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(p.identificacao.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                            SeloOrigem(OrigemProduto.OFICIAL)
                        }
                        if (p.identificacao.complemento.isNotEmpty()) Text(p.identificacao.complemento, style = MaterialTheme.typography.bodySmall)
                        if (p.incompleto(categoria)) Row(Modifier.padding(top = 8.dp)) { Selo("Dados incompletos") }
                        if (p.valores.values.any { it is ValorLido.Incompativel } || p.problemas.isNotEmpty()) {
                            Row(Modifier.padding(top = 4.dp)) { Selo("Formato incompatível", Cores.erroSuave, Cores.erro) }
                        }
                    }
                }
                else -> Unit
            }
        } else {
            estado.erroPessoais?.let { Mensagem(it, Tom.ERRO) }
            Mensagem("Produtos pessoais ficam somente neste dispositivo e não são enviados ao catálogo oficial.")
            BotaoSecundario("Cadastrar produto pessoal", onCadastrar, habilitado = categoria != null)
            if (categoria == null) Mensagem("Definições da categoria indisponíveis: conecte-se para cadastrar.", Tom.AVISO)
            val lista = estado.pessoais
            when {
                lista == null && estado.erroPessoais == null -> Carregando()
                lista.isNullOrEmpty() -> if (estado.erroPessoais == null) Text("Nenhum produto pessoal nesta categoria.")
                else -> lista.forEach { p ->
                    Cartao(onClick = { onDetalhe(OrigemProduto.PESSOAL, p.id) }) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(p.identificacao.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                            SeloOrigem(OrigemProduto.PESSOAL)
                        }
                        if (p.identificacao.complemento.isNotEmpty()) Text(p.identificacao.complemento, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

// ---------------- T03 — Detalhes ----------------

@Composable
fun TelaDetalheProduto(vm: DetalheProdutoViewModel, onVoltar: () -> Unit, onEditar: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val excluido by vm.excluido.collectAsStateWithLifecycle()
    val erroExclusao by vm.erroExclusao.collectAsStateWithLifecycle()
    var confirmar by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(excluido) { if (excluido) onVoltar() }

    val titulo = when (val e = estado) {
        is DetalheUiState.Oficial -> e.produto.identificacao.nome
        is DetalheUiState.Pessoal -> e.produto.identificacao.nome
        else -> "Produto"
    }
    TelaBase(titulo = titulo, onVoltar = onVoltar) {
        when (val e = estado) {
            DetalheUiState.Carregando -> Carregando()
            is DetalheUiState.Indisponivel -> Mensagem(e.mensagem, Tom.ERRO)
            is DetalheUiState.Oficial -> {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { SeloOrigem(OrigemProduto.OFICIAL) }
                if (e.produto.identificacao.complemento.isNotEmpty()) Text(e.produto.identificacao.complemento)
                FaixaOrigemCatalogo(e.origemConsulta, e.produto.atualizadoEm)
                e.produto.problemas.forEach { Mensagem(it, Tom.ERRO) }
                val criterios = e.categoria?.criteriosOrdenados.orEmpty()
                if (criterios.isEmpty()) Mensagem("Definição da categoria indisponível.", Tom.AVISO)
                criterios.forEach { c ->
                    val lido = e.produto.valores[c.id] ?: ValorLido.Ausente
                    LinhaCriterio(
                        c,
                        when (lido) {
                            ValorLido.Ausente -> "Não informado (dado incompleto)"
                            is ValorLido.Valido -> Formatos.valor(lido.valor, c)
                            is ValorLido.Incompativel -> "Formato incompatível: ${lido.motivo}"
                        },
                        e.produto.referencias[c.id],
                        alerta = lido !is ValorLido.Valido,
                    )
                }
                if (criterios.any { it.unidade == "NOTA_1_10" }) {
                    Mensagem("Notas de câmeras/ADAS seguem rubricas da equipe e não são certificações oficiais.")
                }
            }
            is DetalheUiState.Pessoal -> {
                val p = e.produto
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { SeloOrigem(OrigemProduto.PESSOAL) }
                if (p.identificacao.complemento.isNotEmpty()) Text(p.identificacao.complemento)
                Text("Categoria: ${p.categoriaNome} (não pode ser alterada)", style = MaterialTheme.typography.bodySmall)
                p.definicoes.sortedBy { it.ordem }.forEach { c ->
                    LinhaCriterio(c, p.valores[c.id]?.let { Formatos.valor(it, c) } ?: "Não informado", p.referencias[c.id])
                }
                Mensagem("Editar ou excluir este produto não altera avaliações já salvas.")
                erroExclusao?.let { Mensagem(it, Tom.ERRO) }
                BotaoPrincipal("Editar", onEditar)
                BotaoSecundario("Excluir", { confirmar = true })
            }
        }
    }
    if (confirmar) {
        DialogoExclusao(
            objeto = "produto pessoal",
            consequencia = "A exclusão é permanente e não pode ser desfeita. Avaliações salvas continuam com seus dados.",
            onConfirmar = { confirmar = false; vm.excluir() },
            onCancelar = { confirmar = false },
        )
    }
}

@Composable
fun LinhaCriterio(c: CriterioDefinicao, valor: String, prov: Proveniencia?, alerta: Boolean = false) {
    Cartao {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(c.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Selo(
                if (c.natureza == Natureza.BENEFICIO) "Benefício" else "Custo",
                Cores.primariaSuave, Cores.primaria,
            )
        }
        Text(valor, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = if (alerta) Cores.aviso else Cores.tinta)
        Text("Unidade: ${Unidades.rotulo(c.unidade)}", style = MaterialTheme.typography.bodySmall)
        TextoProveniencia(prov)
    }
}

@Composable
fun TextoProveniencia(prov: Proveniencia?) {
    val p = prov ?: Proveniencia.DESCONHECIDA
    val partes = buildList {
        add("Fonte: " + (p.fonte ?: "desconhecida"))
        p.dataReferencia?.let { add("referência: $it") }
        p.protocoloOuRubrica?.let { add("protocolo/rubrica: $it") }
        add(when (p.estimativa) { true -> "estimado"; false -> "não estimado"; null -> "estimativa desconhecida" })
        if (p.ajustadoNaAvaliacao) add("ajustado na avaliação")
    }
    Spacer(Modifier.height(4.dp))
    Text(partes.joinToString(" · "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}
