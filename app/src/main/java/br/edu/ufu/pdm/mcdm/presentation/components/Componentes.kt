package br.edu.ufu.pdm.mcdm.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.ufu.pdm.mcdm.domain.model.EstadoConsulta
import br.edu.ufu.pdm.mcdm.domain.model.OrigemDados
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.presentation.theme.Cores
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class Tom { INFO, SUCESSO, AVISO, ERRO }

/** Mensagem com texto e símbolo (não depende só da cor); falhas permanecem na tela. */
@Composable
fun Mensagem(texto: String, tom: Tom = Tom.INFO, modifier: Modifier = Modifier) {
    val (fundo, cor, icone) = when (tom) {
        Tom.INFO -> Triple(Cores.primariaSuave, Cores.tinta, Icons.Outlined.Info)
        Tom.SUCESSO -> Triple(Cores.sucessoSuave, Cores.sucesso, Icons.Outlined.CheckCircle)
        Tom.AVISO -> Triple(Cores.avisoSuave, Cores.aviso, Icons.Outlined.WarningAmber)
        Tom.ERRO -> Triple(Cores.erroSuave, Cores.erro, Icons.Outlined.ErrorOutline)
    }
    Row(
        modifier
            .fillMaxWidth()
            .background(fundo, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .semantics { liveRegion = if (tom == Tom.ERRO) LiveRegionMode.Assertive else LiveRegionMode.Polite },
        verticalAlignment = Alignment.Top,
    ) {
        Icon(icone, contentDescription = null, tint = cor, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(texto, color = cor, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun Cartao(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, conteudo: @Composable () -> Unit) {
    val cores = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val elevacao = CardDefaults.cardElevation(defaultElevation = 0.dp)
    val shape = RoundedCornerShape(20.dp)
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier.fillMaxWidth(), shape = shape, colors = cores, elevation = elevacao) {
            Column(Modifier.padding(16.dp)) { conteudo() }
        }
    } else {
        Card(modifier = modifier.fillMaxWidth(), shape = shape, colors = cores, elevation = elevacao) {
            Column(Modifier.padding(16.dp)) { conteudo() }
        }
    }
}

/** Rótulo de origem textual (oficial/pessoal) — a cor apenas reforça. */
@Composable
fun SeloOrigem(origem: OrigemProduto) {
    val pessoal = origem == OrigemProduto.PESSOAL
    Selo(if (pessoal) "Pessoal" else "Oficial", if (pessoal) Cores.pessoalSuave else Cores.primariaSuave, if (pessoal) Cores.pessoal else Cores.primaria)
}

@Composable
fun Selo(texto: String, fundo: Color = Cores.avisoSuave, cor: Color = Cores.aviso) {
    Text(
        texto,
        color = cor,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.background(fundo, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 3.dp),
    )
}

@Composable
fun BotaoPrincipal(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, habilitado: Boolean = true) {
    Button(
        onClick = onClick, enabled = habilitado, shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth().heightIn(min = 52.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    ) { Text(texto) }
}

@Composable
fun BotaoSecundario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, habilitado: Boolean = true) {
    OutlinedButton(
        onClick = onClick, enabled = habilitado, shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth().heightIn(min = 52.dp),
    ) { Text(texto) }
}

@Composable
fun Carregando(texto: String = "Carregando…") {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CircularProgressIndicator()
            Text(texto, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private val dataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).withZone(ZoneId.systemDefault())
fun formatarInstante(i: Instant): String = dataHora.format(i)

/**
 * Origem e datas do catálogo oficial (DEC-020): cache × servidor, atualização do publicador e
 * confirmação no servidor têm rótulos distintos; datas desconhecidas não são inferidas.
 */
@Composable
fun FaixaOrigemCatalogo(estado: EstadoConsulta<*>, atualizadoPeloPublicador: Instant?) {
    when (estado) {
        EstadoConsulta.Carregando -> Mensagem("Consultando o catálogo oficial…")
        EstadoConsulta.PermissaoNegada -> Mensagem("Acesso ao catálogo oficial negado pelo servidor.", Tom.ERRO)
        is EstadoConsulta.Indisponivel -> Mensagem("Catálogo oficial indisponível no momento. ${estado.mensagem}", Tom.ERRO)
        is EstadoConsulta.Dados<*> -> {
            val vazio = (estado.dados as? Collection<*>)?.isEmpty() == true
            val publicador = atualizadoPeloPublicador?.let { "Atualização do publicador: ${formatarInstante(it)}." }
                ?: "Data de atualização do publicador desconhecida."
            val texto = when {
                estado.origem == OrigemDados.CACHE && vazio ->
                    "Sem dados oficiais no cache deste dispositivo. Isso não indica catálogo vazio: conecte-se para consultar."
                estado.origem == OrigemDados.CACHE ->
                    "Exibindo dados do cache do dispositivo (podem estar desatualizados). $publicador" +
                        (estado.confirmadoNoServidorEm?.let { " Última confirmação no servidor nesta sessão: ${formatarInstante(it)}." } ?: "")
                vazio -> "Consulta confirmada no servidor: nenhum registro oficial publicado."
                else -> "Confirmado no servidor ${estado.confirmadoNoServidorEm?.let { formatarInstante(it) } ?: ""}. $publicador"
            }
            Mensagem(texto, if (estado.origem == OrigemDados.CACHE) Tom.AVISO else Tom.INFO)
            if (estado.documentosInvalidos > 0) {
                Spacer(Modifier.size(8.dp))
                Mensagem("${estado.documentosInvalidos} documento(s) fora do contrato foram ignorados.", Tom.AVISO)
            }
        }
    }
}

/** Diálogo de confirmação de exclusão permanente (D03); foco inicial em Cancelar. */
@Composable
fun DialogoExclusao(objeto: String, consequencia: String, onConfirmar: () -> Unit, onCancelar: () -> Unit) {
    val foco = remember { FocusRequester() }
    LaunchedEffect(Unit) { runCatching { foco.requestFocus() } }
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Excluir $objeto?") },
        text = { Text(consequencia) },
        confirmButton = {
            TextButton(onClick = onConfirmar) { Text("Excluir permanentemente", color = Cores.erro) }
        },
        dismissButton = {
            TextButton(onClick = onCancelar, modifier = Modifier.focusRequester(foco)) { Text("Cancelar") }
        },
    )
}

/** Diálogo D01: Salvar / Descartar alterações / Continuar editando. */
@Composable
fun DialogoSaida(
    salvando: Boolean,
    erro: String?,
    onSalvar: () -> Unit,
    onDescartar: () -> Unit,
    onContinuar: () -> Unit,
    descricaoSalvar: String = "Salvar",
) {
    AlertDialog(
        onDismissRequest = onContinuar,
        title = { Text("Salvar alterações?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Há alterações não salvas. Nada é salvo automaticamente.")
                erro?.let { Mensagem(it, Tom.ERRO) }
            }
        },
        confirmButton = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                BotaoPrincipal(if (salvando) "Salvando…" else descricaoSalvar, onSalvar, habilitado = !salvando)
                BotaoSecundario("Descartar alterações", onDescartar, habilitado = !salvando)
                TextButton(onClick = onContinuar, modifier = Modifier.fillMaxWidth()) { Text("Continuar editando") }
            }
        },
    )
}
