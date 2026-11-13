package br.edu.ufu.pdm.mcdm.presentation.avaliacao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.Unidades
import br.edu.ufu.pdm.mcdm.domain.usecase.Elegibilidade
import br.edu.ufu.pdm.mcdm.domain.validation.Formatos
import br.edu.ufu.pdm.mcdm.presentation.catalogo.TextoProveniencia
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoPrincipal
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoSecundario
import br.edu.ufu.pdm.mcdm.presentation.components.Cartao
import br.edu.ufu.pdm.mcdm.presentation.components.FaixaOrigemCatalogo
import br.edu.ufu.pdm.mcdm.presentation.components.Mensagem
import br.edu.ufu.pdm.mcdm.presentation.components.Selo
import br.edu.ufu.pdm.mcdm.presentation.components.SeloOrigem
import br.edu.ufu.pdm.mcdm.presentation.components.Tom
import br.edu.ufu.pdm.mcdm.presentation.theme.Cores

// ---------------- T06 — Seleção de alternativas ----------------

@Composable
fun TelaSelecao(vm: AvaliacaoViewModel, onVoltar: () -> Unit, onCadastrarPessoal: () -> Unit, onContinuar: () -> Unit) {
    val s by vm.estado.collectAsStateWithLifecycle()
    val (estadoOficiais, candidatos) = vm.candidatos.collectAsStateWithLifecycle().value
    val alternativas = s.edicao?.conteudo?.alternativasOrdenadas.orEmpty()
    val selecionadas = alternativas.map { it.chaveOrigem }.toSet()

    TelaSessao(
        vm, Passo.SELECAO, "Selecionar alternativas", onVoltar,
        rodape = {
            if (alternativas.size < 2) Text("Selecione pelo menos duas alternativas para continuar.", style = MaterialTheme.typography.bodySmall)
            BotaoPrincipal("Continuar para valores (${alternativas.size})", onContinuar, habilitado = alternativas.size >= 2)
        },
    ) {
        OutlinedTextField(
            value = s.edicao?.conteudo?.titulo.orEmpty(),
            onValueChange = vm::alterarTitulo,
            label = { Text("Título da avaliação (opcional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Selecionadas", style = MaterialTheme.typography.titleMedium)
        if (alternativas.isEmpty()) Text("Nenhuma alternativa selecionada.", style = MaterialTheme.typography.bodyMedium)
        alternativas.forEach { a ->
            Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Check, contentDescription = null, tint = Cores.primaria)
                    Text(a.identificacao.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f).padding(start = 8.dp))
                    SeloOrigem(a.origemProduto)
                    IconButton(onClick = { vm.remover(a.alternativaId) }) {
                        Icon(Icons.Outlined.RemoveCircleOutline, contentDescription = "Remover ${a.identificacao.nome}")
                    }
                }
                Text("Dados copiados para esta avaliação; mudanças no catálogo não a alteram.", style = MaterialTheme.typography.bodySmall)
            }
        }

        Text("Disponíveis na categoria", style = MaterialTheme.typography.titleMedium)
        FaixaOrigemCatalogo(estadoOficiais, null)
        BotaoSecundario("Cadastrar produto pessoal", onCadastrarPessoal)
        candidatos.filter { it.chave !in selecionadas }.forEach { c ->
            val bloqueio = c.elegibilidade as? Elegibilidade.Bloqueada
            Cartao(onClick = if (bloqueio == null) ({ vm.adicionar(c) }) else null) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(c.identificacao.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    SeloOrigem(c.origem)
                    if (bloqueio == null) Icon(Icons.Outlined.Add, contentDescription = "Adicionar ${c.identificacao.nome}", tint = Cores.primaria)
                }
                if (c.identificacao.complemento.isNotEmpty()) Text(c.identificacao.complemento, style = MaterialTheme.typography.bodySmall)
                when (val el = c.elegibilidade) {
                    is Elegibilidade.Bloqueada -> Mensagem("Não pode ser selecionado: ${el.motivo}", Tom.ERRO, Modifier.padding(top = 8.dp))
                    is Elegibilidade.Elegivel -> el.aviso?.let { Row(Modifier.padding(top = 8.dp)) { Selo(it) } }
                }
            }
        }
    }
}

// ---------------- T07 — Valores da avaliação ----------------

@Composable
fun TelaValores(vm: AvaliacaoViewModel, onVoltar: () -> Unit) {
    val s by vm.estado.collectAsStateWithLifecycle()
    val eventos by vm.eventos.collectAsStateWithLifecycle()
    val conteudo = s.edicao?.conteudo
    val criterios = conteudo?.criteriosOrdenados.orEmpty()
    val chaves = conteudo?.alternativasOrdenadas.orEmpty().flatMap { a -> criterios.map { chaveValor(a.alternativaId, it.id) } }
    val focos = remember(chaves) { chaves.associateWith { FocusRequester() } }

    // Foco e rolagem até o primeiro campo inválido.
    eventos.filterIsInstance<EventoSessao.FocarValor>().firstOrNull()?.let { ev ->
        LaunchedEffect(ev) {
            runCatching { focos[ev.chave]?.requestFocus() }
            vm.consumir(ev)
        }
    }

    TelaSessao(
        vm, Passo.VALORES, "Valores da avaliação", onVoltar,
        rodape = { BotaoPrincipal("Continuar para comparações", vm::validarValoresEAvancar) },
    ) {
        Mensagem("Ajustes valem só para esta avaliação; os catálogos não são alterados. Ausente não vale zero.")
        conteudo?.alternativasOrdenadas?.forEach { a ->
            Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(a.identificacao.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    SeloOrigem(a.origemProduto)
                }
                criterios.forEach { c ->
                    val chave = chaveValor(a.alternativaId, c.id)
                    val v = a.valores[c.id]
                    val erro = s.errosValores[chave]
                    OutlinedTextField(
                        value = s.textos[chave].orEmpty(),
                        onValueChange = { vm.alterarValor(a.alternativaId, c, it) },
                        label = { Text("${c.nome} (${Unidades.rotulo(c.unidade)})") },
                        isError = erro != null,
                        supportingText = {
                            Text(
                                erro ?: ("Original: " + Formatos.valor(v?.origem, c) + " · " + if (c.natureza == Natureza.BENEFICIO) "benefício" else "custo"),
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp).focusRequester(focos.getValue(chave)),
                    )
                    if (v?.ajustado == true) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Selo("Ajustado nesta avaliação", Cores.pessoalSuave, Cores.pessoal)
                            if (v.origem != null) TextButton(onClick = { vm.restaurarOriginal(a.alternativaId, c) }) { Text("Restaurar original") }
                        }
                    } else {
                        TextoProveniencia(v?.provenienciaOrigem)
                    }
                }
            }
        }
        Text("Use vírgula para decimais; ponto só separa milhares (5.000,00).", style = MaterialTheme.typography.bodySmall)
    }
}
