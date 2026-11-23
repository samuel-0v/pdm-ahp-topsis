package br.edu.ufu.pdm.mcdm.presentation.avaliacao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.edu.ufu.pdm.mcdm.domain.exportacao.ConteudoRelatorio
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.ResultadoTopsis
import br.edu.ufu.pdm.mcdm.domain.model.Unidades
import br.edu.ufu.pdm.mcdm.domain.model.Vigencia
import br.edu.ufu.pdm.mcdm.domain.validation.Formatos
import br.edu.ufu.pdm.mcdm.presentation.catalogo.TextoProveniencia
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoPrincipal
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoSecundario
import br.edu.ufu.pdm.mcdm.presentation.components.Cartao
import br.edu.ufu.pdm.mcdm.presentation.components.DialogoExclusao
import br.edu.ufu.pdm.mcdm.presentation.components.Mensagem
import br.edu.ufu.pdm.mcdm.presentation.components.Selo
import br.edu.ufu.pdm.mcdm.presentation.components.SeloOrigem
import br.edu.ufu.pdm.mcdm.presentation.components.Tom
import br.edu.ufu.pdm.mcdm.presentation.theme.Cores

// ---------------- T10/T11 — Revisão e processamento ----------------

@Composable
fun TelaRevisao(vm: AvaliacaoViewModel, onVoltar: () -> Unit, onEditarValores: () -> Unit, onEditarAhp: () -> Unit) {
    val s by vm.estado.collectAsStateWithLifecycle()
    val e = s.edicao
    val problemas = remember(e) { vm.problemasRevisao() }
    TelaSessao(
        vm, Passo.REVISAO, "Revisão", onVoltar,
        rodape = {
            if (s.processando) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator(Modifier.padding(4.dp)); Text("Processando TOPSIS…")
                }
            }
            BotaoPrincipal("Processar TOPSIS", vm::processar, habilitado = problemas.isEmpty() && !s.processando)
        },
    ) {
        if (e == null) return@TelaSessao
        val c = e.conteudo
        (s.problemas.ifEmpty { problemas }).forEach { Mensagem(it.mensagem, Tom.ERRO) }
        if (problemas.isEmpty() && s.problemas.isEmpty()) Mensagem("Entradas completas e AHP consistente. Pronto para processar.", Tom.SUCESSO)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onEditarValores) { Text("Editar valores") }
            TextButton(onClick = onEditarAhp) { Text("Editar comparações") }
        }
        Text("Critérios e pesos", style = MaterialTheme.typography.titleMedium)
        Cartao {
            c.criteriosOrdenados.forEach { cr ->
                Text(
                    "${cr.nome} · ${Unidades.rotulo(cr.unidade)} · ${if (cr.natureza == Natureza.BENEFICIO) "benefício" else "custo"} · " +
                        (e.ahpVigente?.pesos?.get(cr.id)?.let(Formatos::percentual) ?: "peso pendente"),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            e.ahpVigente?.let { Text("RC ${it.rc?.let(Formatos::rc) ?: "não definida"} · λmax ${Formatos.fixo(it.lambdaMax, 4)}", style = MaterialTheme.typography.bodySmall) }
        }
        Text("Alternativas (${c.alternativas.size})", style = MaterialTheme.typography.titleMedium)
        c.alternativasOrdenadas.forEach { a ->
            Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(a.identificacao.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    SeloOrigem(a.origemProduto)
                }
                c.criteriosOrdenados.forEach { cr ->
                    val v = a.valores[cr.id]
                    Text("${cr.nome}: ${Formatos.valor(v?.efetivo, cr)}${if (v?.ajustado == true) " (ajustado)" else ""}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

// ---------------- T12 — Resultado ----------------

@Composable
fun TelaResultado(
    vm: AvaliacaoViewModel,
    onVoltar: () -> Unit,
    onDetalhes: () -> Unit,
    onRevisao: () -> Unit,
    onEditarValores: () -> Unit,
) {
    val s by vm.estado.collectAsStateWithLifecycle()
    var dialogoExportar by rememberSaveable { mutableStateOf(false) }
    var confirmarExclusao by rememberSaveable { mutableStateOf(false) }
    val e = s.edicao
    val topsis = e?.topsisVigente

    TelaSessao(
        vm, Passo.RESULTADO, "Resultado", onVoltar,
        rodape = {
            if (topsis != null && s.pendente) BotaoPrincipal(if (s.salvando) "Salvando…" else "Salvar avaliação", { vm.salvar() }, habilitado = !s.salvando)
            BotaoSecundario(
                if (s.exportando) "Gerando PDF…" else "Exportar PDF",
                { if (s.pendente) dialogoExportar = true else vm.exportarSalva() },
                habilitado = !s.exportando && (vm.podeExportarSalva || vm.podeSalvarEExportar),
            )
        },
    ) {
        if (e == null) return@TelaSessao
        if (topsis == null) {
            Mensagem(
                if (e.vigenciaTopsis == Vigencia.DESATUALIZADO) "O resultado anterior está desatualizado: as entradas mudaram. Processe novamente na revisão."
                else "Ainda não há resultado TOPSIS para esta avaliação.",
                Tom.AVISO,
            )
            BotaoSecundario("Ir para a revisão", onRevisao)
            return@TelaSessao
        }
        Mensagem(
            if (s.pendente) "Resultado calculado. Ainda não salvo." else "Versão salva neste dispositivo.",
            if (s.pendente) Tom.AVISO else Tom.SUCESSO,
        )
        val alts = e.conteudo.alternativas.associateBy { it.alternativaId }
        when (topsis) {
            is ResultadoTopsis.SemDiferenciacao -> {
                Text("Sem diferenciação entre as alternativas", style = MaterialTheme.typography.headlineSmall)
                Text("Os valores efetivos ponderados são iguais: d+ = d− = 0. Não há vencedor, coeficiente C* ou posição definidos.")
                e.conteudo.alternativasOrdenadas.forEach { a ->
                    val d = topsis.distancias.getValue(a.alternativaId)
                    Cartao {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(a.identificacao.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                            SeloOrigem(a.origemProduto)
                        }
                        Text("d+ ${Formatos.fixo(d.dMais, 4)} · d− ${Formatos.fixo(d.dMenos, 4)} · C* não definido")
                    }
                }
            }
            is ResultadoTopsis.Ranking -> {
                val ordenados = topsis.detalhes.entries.sortedWith(compareBy({ it.value.posicao }, { alts.getValue(it.key).ordem }))
                val primeiros = topsis.tamanhoGrupo(1)
                Text(
                    if (primeiros == 1) alts.getValue(ordenados.first().key).identificacao.nome else "Empate na primeira posição",
                    style = MaterialTheme.typography.headlineSmall,
                )
                if (primeiros > 1) Text("$primeiros alternativas compartilham a 1ª posição; nenhuma é eleita vencedora.")
                ordenados.forEach { (id, d) ->
                    val a = alts.getValue(id)
                    val grupo = topsis.tamanhoGrupo(d.posicao)
                    val destaque = d.posicao == 1 && primeiros == 1
                    Cartao {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("${d.posicao}º", style = MaterialTheme.typography.headlineSmall, color = if (destaque) Cores.primaria else Cores.tinta)
                            Column(Modifier.weight(1f)) {
                                Text(a.identificacao.nome, style = MaterialTheme.typography.titleMedium, fontWeight = if (destaque) FontWeight.Bold else FontWeight.Medium)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    SeloOrigem(a.origemProduto)
                                    if (grupo > 1) Selo("Empate · $grupo alternativas")
                                }
                            }
                        }
                        Text("C* ${Formatos.fixo(d.coeficiente, 4)} · d+ ${Formatos.fixo(d.dMais, 4)} · d− ${Formatos.fixo(d.dMenos, 4)}", modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
        e.ahpVigente?.let { ahp ->
            Text(
                e.conteudo.criteriosOrdenados.joinToString(" · ") { "${it.nome} ${Formatos.percentual(ahp.pesos.getValue(it.id))}" },
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onDetalhes) { Text("Detalhes") }
            TextButton(onClick = onEditarValores) { Text("Editar avaliação") }
            if (s.salva != null) TextButton(onClick = { confirmarExclusao = true }) { Text("Excluir", color = Cores.erro) }
        }
    }

    if (dialogoExportar) {
        DialogoExportacao(
            podeSalvarEExportar = vm.podeSalvarEExportar,
            podeExportarSalva = vm.podeExportarSalva,
            onSalvarEExportar = { dialogoExportar = false; vm.salvarEExportar() },
            onExportarSalva = { dialogoExportar = false; vm.exportarSalva() },
            onCancelar = { dialogoExportar = false },
        )
    }
    if (confirmarExclusao) {
        DialogoExclusao(
            objeto = "avaliação",
            consequencia = "A versão salva e seus resultados serão removidos permanentemente. Os catálogos não são afetados.",
            onConfirmar = { confirmarExclusao = false; vm.excluirSalva() },
            onCancelar = { confirmarExclusao = false },
        )
    }
}

/** D02 — exportação com alterações pendentes (DEC-032). */
@Composable
private fun DialogoExportacao(
    podeSalvarEExportar: Boolean,
    podeExportarSalva: Boolean,
    onSalvarEExportar: () -> Unit,
    onExportarSalva: () -> Unit,
    onCancelar: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Exportar PDF") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Há alterações não salvas. O PDF é gerado somente a partir de uma versão salva e processada.")
                if (!podeSalvarEExportar) Mensagem("A edição atual precisa ser processada antes de ser salva e exportada.", Tom.AVISO)
                if (!podeExportarSalva) Mensagem("Não há versão anterior salva e processada.", Tom.INFO)
            }
        },
        confirmButton = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                BotaoPrincipal("Salvar e exportar", onSalvarEExportar, habilitado = podeSalvarEExportar)
                BotaoSecundario("Exportar última versão salva", onExportarSalva, habilitado = podeExportarSalva)
                TextButton(onClick = onCancelar, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
            }
        },
    )
}

// ---------------- T12-DETALHES ----------------

@Composable
fun TelaDetalhesAvaliacao(vm: AvaliacaoViewModel, onVoltar: () -> Unit) {
    val s by vm.estado.collectAsStateWithLifecycle()
    val e = s.edicao
    TelaSessao(vm, Passo.RESULTADO, "Detalhes da avaliação", onVoltar) {
        if (e == null) return@TelaSessao
        e.ahpVigente?.let { ahp ->
            Text("Indicadores AHP", style = MaterialTheme.typography.titleMedium)
            Cartao {
                Text("λmax ${Formatos.fixo(ahp.lambdaMax, 6)} · IC ${Formatos.fixo(ahp.ic, 6)} · RI ${Formatos.fixo(ahp.ri, 2)} · RC ${ahp.rc?.let(Formatos::rc) ?: "não definida"}")
                e.conteudo.criteriosOrdenados.forEach { Text("${it.nome}: ${Formatos.percentual(ahp.pesos.getValue(it.id))}", style = MaterialTheme.typography.bodyMedium) }
            }
        }
        Text("Valores utilizados", style = MaterialTheme.typography.titleMedium)
        e.conteudo.alternativasOrdenadas.forEach { a ->
            Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(a.identificacao.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    SeloOrigem(a.origemProduto)
                }
                if (a.identificacao.complemento.isNotEmpty()) Text(a.identificacao.complemento, style = MaterialTheme.typography.bodySmall)
                e.conteudo.criteriosOrdenados.forEach { c ->
                    val v = a.valores[c.id]
                    Text(c.nome, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 8.dp))
                    Text("Efetivo: ${Formatos.valor(v?.efetivo, c)}${if (v?.ajustado == true) " (ajustado)" else ""}")
                    if (v?.ajustado == true) Text("Original: ${Formatos.valor(v.origem, c)}", style = MaterialTheme.typography.bodySmall)
                    TextoProveniencia(v?.provenienciaOrigem)
                }
            }
        }
        Text("Método", style = MaterialTheme.typography.titleMedium)
        Text(ConteudoRelatorio.METODO, style = MaterialTheme.typography.bodySmall)
    }
}
