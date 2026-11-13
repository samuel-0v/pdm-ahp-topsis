package br.edu.ufu.pdm.mcdm.presentation.avaliacao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.Julgamento
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.ParCriterios
import br.edu.ufu.pdm.mcdm.domain.model.Vigencia
import br.edu.ufu.pdm.mcdm.domain.validation.Formatos
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoPrincipal
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoSecundario
import br.edu.ufu.pdm.mcdm.presentation.components.Cartao
import br.edu.ufu.pdm.mcdm.presentation.components.Mensagem
import br.edu.ufu.pdm.mcdm.presentation.components.Tom
import br.edu.ufu.pdm.mcdm.presentation.theme.Cores

private val escala = mapOf(
    2 to "entre igual e moderada", 3 to "moderada", 4 to "entre moderada e forte", 5 to "forte",
    6 to "entre forte e muito forte", 7 to "muito forte", 8 to "entre muito forte e extrema", 9 to "extrema",
)

fun descreverJulgamento(j: Julgamento?, a: CriterioDefinicao, b: CriterioDefinicao): String = when {
    j == null -> "Pendente"
    j.preferidoId == null -> "Igual importância (1)"
    else -> "${if (j.preferidoId == a.id) a.nome else b.nome} · ${escala[j.intensidade]} (${j.intensidade})"
}

// ---------------- T08 — Comparação AHP ----------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TelaAhp(vm: AvaliacaoViewModel, onVoltar: () -> Unit) {
    val s by vm.estado.collectAsStateWithLifecycle()
    val conteudo = s.edicao?.conteudo ?: run { TelaSessao(vm, Passo.AHP, "Comparações AHP", onVoltar) {}; return }
    val pares = conteudo.paresEsperados
    val respondidos = pares.count { (a, b) -> ParCriterios.de(a.id, b.id) in conteudo.julgamentos }
    var indice by rememberSaveable { mutableIntStateOf(pares.indexOfFirst { (a, b) -> ParCriterios.de(a.id, b.id) !in conteudo.julgamentos }.coerceAtLeast(0)) }
    var lista by rememberSaveable { mutableStateOf(false) }
    // Lado escolhido antes da intensidade: o par continua pendente (sem 1 implícito).
    var ladoProvisorio by rememberSaveable(indice) { mutableStateOf<String?>(null) }

    // Voltar no primeiro par retorna aos valores; nos demais, ao par anterior.
    val voltar: () -> Unit = { if (!lista && indice > 0) indice-- else onVoltar() }

    TelaSessao(
        vm, Passo.AHP, "Comparações AHP", voltar,
        rodape = {
            if (respondidos == pares.size) {
                BotaoPrincipal(if (s.calculandoAhp) "Calculando…" else "Calcular pesos e consistência", vm::calcularPesos, habilitado = !s.calculandoAhp)
            } else {
                Text("Responda os ${pares.size} pares para calcular os pesos. Você pode salvar um rascunho a qualquer momento.", style = MaterialTheme.typography.bodySmall)
            }
        },
    ) {
        Text("Respondidos: $respondidos de ${pares.size}", style = MaterialTheme.typography.titleMedium)
        LinearProgressIndicator(progress = { respondidos / pares.size.toFloat() }, modifier = Modifier.fillMaxWidth())
        s.problemas.forEach { Mensagem(it.mensagem, Tom.AVISO) }
        if (s.edicao?.vigenciaAhp == Vigencia.DESATUALIZADO) {
            Mensagem("Os julgamentos mudaram: os pesos anteriores estão desatualizados e precisam ser recalculados.", Tom.AVISO)
        }
        TextButton(onClick = { lista = !lista }) { Text(if (lista) "Responder par a par" else "Ver todos os pares") }

        if (lista) {
            pares.forEachIndexed { i, (a, b) ->
                val j = conteudo.julgamentos[ParCriterios.de(a.id, b.id)]
                Cartao(onClick = { indice = i; lista = false }) {
                    Text("${i + 1}. ${a.nome} × ${b.nome}", style = MaterialTheme.typography.titleMedium)
                    Text(descreverJulgamento(j, a, b), color = if (j == null) Cores.aviso else Cores.tinta)
                }
            }
            return@TelaSessao
        }

        val (a, b) = pares[indice]
        val atual = conteudo.julgamentos[ParCriterios.de(a.id, b.id)]
        val lado = ladoProvisorio ?: atual?.let { it.preferidoId ?: "IGUAL" }
        Cartao {
            Text("Par ${indice + 1} de ${pares.size}", style = MaterialTheme.typography.labelSmall)
            Text("Qual critério é mais importante nesta avaliação?", style = MaterialTheme.typography.titleMedium)
            Text("Situação: ${descreverJulgamento(atual, a, b)}", style = MaterialTheme.typography.bodySmall, color = if (atual == null) Cores.aviso else Cores.sucesso)
            Box(Modifier.padding(top = 8.dp).selectableGroup()) {
                androidx.compose.foundation.layout.Column {
                    OpcaoLado(a.nome + naturezaCurta(a), lado == a.id) { ladoProvisorio = a.id }
                    OpcaoLado("Igual importância", lado == "IGUAL") { ladoProvisorio = null; vm.responder(a.id, b.id, null, 1) }
                    OpcaoLado(b.nome + naturezaCurta(b), lado == b.id) { ladoProvisorio = b.id }
                }
            }
            if (lado != null && lado != "IGUAL") {
                Text("Intensidade da preferência", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (2..9).forEach { n ->
                        val marcado = atual?.preferidoId == lado && atual.intensidade == n
                        FilterChip(
                            selected = marcado,
                            onClick = { vm.responder(a.id, b.id, lado, n); ladoProvisorio = null },
                            label = { Text("$n") },
                            modifier = Modifier.heightIn(min = 48.dp).widthIn(min = 48.dp),
                        )
                    }
                }
                Text("Escala: 1 igual, 3 moderada, 5 forte, 7 muito forte, 9 extrema; 2, 4, 6 e 8 intermediárias.", style = MaterialTheme.typography.bodySmall)
                if (ladoProvisorio != null) Text("Escolha a intensidade para registrar a resposta.", color = Cores.aviso, style = MaterialTheme.typography.bodySmall)
            }
        }
        Mensagem("Importância do preço não significa que preço maior é melhor: preço continua sendo custo.")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BotaoSecundario("Par anterior", { indice-- }, Modifier.weight(1f), habilitado = indice > 0)
            BotaoSecundario("Próximo par", { indice++ }, Modifier.weight(1f), habilitado = indice < pares.size - 1)
        }
    }

    // Ao concluir uma resposta, segue para o próximo par pendente.
    LaunchedEffect(respondidos) {
        if (!lista && conteudo.julgamentos[ParCriterios.de(pares[indice].first.id, pares[indice].second.id)] != null && ladoProvisorio == null) {
            val proximo = pares.indices.firstOrNull { i -> i > indice && ParCriterios.de(pares[i].first.id, pares[i].second.id) !in conteudo.julgamentos }
            if (proximo != null) indice = proximo
        }
    }
}

private fun naturezaCurta(c: CriterioDefinicao) = if (c.natureza == Natureza.CUSTO) " (custo)" else ""

@Composable
private fun OpcaoLado(texto: String, marcado: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 48.dp).selectable(marcado, onClick = onClick, role = Role.RadioButton),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = marcado, onClick = null)
        Text(texto, modifier = Modifier.padding(start = 8.dp))
    }
}

// ---------------- T09 — Pesos e consistência ----------------

@Composable
fun TelaPesos(vm: AvaliacaoViewModel, onVoltar: () -> Unit, onRevisar: () -> Unit) {
    val s by vm.estado.collectAsStateWithLifecycle()
    val e = s.edicao
    val ahp = e?.ahpVigente
    TelaSessao(
        vm, Passo.PESOS, "Pesos e consistência", onVoltar,
        rodape = {
            when {
                ahp == null -> BotaoPrincipal("Recalcular pesos", vm::calcularPesos, habilitado = e?.conteudo?.julgamentosCompletos == true && !s.calculandoAhp)
                ahp.consistente -> BotaoPrincipal("Revisar e processar", onRevisar)
                else -> {
                    BotaoPrincipal("Revisar julgamentos", onVoltar)
                    BotaoSecundario("Salvar rascunho", { vm.salvar() })
                }
            }
        },
    ) {
        if (ahp == null) {
            Mensagem(
                if (e?.vigenciaAhp == Vigencia.DESATUALIZADO) "Os pesos estão desatualizados: os julgamentos mudaram." else "Pesos ainda não calculados.",
                Tom.AVISO,
            )
            return@TelaSessao
        }
        if (ahp.consistente) {
            Mensagem("Julgamentos consistentes (RC ≤ 0,10). O TOPSIS pode ser processado.", Tom.SUCESSO)
        } else {
            Mensagem("Julgamentos inconsistentes (RC > 0,10). O TOPSIS fica bloqueado: revise as comparações ou salve como rascunho.", Tom.ERRO)
        }
        Cartao {
            Text("RC ${ahp.rc?.let { Formatos.rc(it) } ?: "não definida"}", style = MaterialTheme.typography.headlineSmall, color = if (ahp.consistente) Cores.sucesso else Cores.erro)
            Text("λmax ${Formatos.fixo(ahp.lambdaMax, 4)} · IC ${Formatos.fixo(ahp.ic, 4)} · RI ${Formatos.fixo(ahp.ri, 2)}", style = MaterialTheme.typography.bodyMedium)
        }
        e.conteudo.criteriosOrdenados.forEach { c ->
            val peso = ahp.pesos.getValue(c.id)
            Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(c.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    Text(Formatos.percentual(peso), fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(progress = { peso.toFloat() }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            }
        }
        Text("Pesos pela média geométrica normalizada (DEC-018). RI = ${Formatos.fixo(ahp.ri, 2)} para ${ahp.pesos.size} critérios.", style = MaterialTheme.typography.bodySmall)
    }
}
