package br.edu.ufu.pdm.mcdm.presentation.avaliacao

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.edu.ufu.pdm.mcdm.presentation.components.Carregando
import br.edu.ufu.pdm.mcdm.presentation.components.DialogoSaida
import br.edu.ufu.pdm.mcdm.presentation.components.Mensagem
import br.edu.ufu.pdm.mcdm.presentation.components.TelaBase
import br.edu.ufu.pdm.mcdm.presentation.components.Tom
import br.edu.ufu.pdm.mcdm.presentation.theme.Cores

private val etapas = listOf(Passo.SELECAO to "Seleção", Passo.VALORES to "Valores", Passo.AHP to "AHP", Passo.REVISAO to "Revisão", Passo.RESULTADO to "Resultado")

/**
 * Estrutura comum aos passos da sessão: barra com estado de salvamento, ação Salvar,
 * Fechar (aplica D01) e Voltar do sistema conforme a tabela de navegação.
 */
@Composable
fun TelaSessao(
    vm: AvaliacaoViewModel,
    passo: Passo,
    titulo: String,
    onVoltar: () -> Unit,
    rodape: (@Composable ColumnScope.() -> Unit)? = null,
    conteudo: @Composable ColumnScope.() -> Unit,
) {
    val s by vm.estado.collectAsStateWithLifecycle()
    val dialogo by vm.dialogoSaida.collectAsStateWithLifecycle()
    BackHandler(onBack = onVoltar)

    val estadoSalvo = when {
        s.salva == null -> "Ainda não salva"
        s.pendente -> "Alterações não salvas"
        else -> "Salva neste dispositivo"
    }
    TelaBase(
        titulo = titulo,
        subtitulo = listOfNotNull(s.edicao?.conteudo?.categoriaNome, estadoSalvo).joinToString(" · "),
        onVoltar = onVoltar,
        acoes = {
            if (s.edicao != null) {
                IconButton(onClick = { vm.salvar() }, enabled = !s.salvando) { Icon(Icons.Outlined.Save, contentDescription = "Salvar") }
            }
            IconButton(onClick = vm::pedirSaida) { Icon(Icons.Outlined.Close, contentDescription = "Encerrar avaliação") }
        },
        rodape = if (s.edicao != null) rodape else null,
    ) {
        when {
            s.carregando -> Carregando("Abrindo avaliação…")
            s.erroCarga != null -> Mensagem(s.erroCarga!!, Tom.ERRO)
            else -> {
                IndicadorEtapas(passo)
                s.mensagem?.let { Mensagem(it.first, it.second) }
                conteudo()
            }
        }
    }

    if (dialogo) {
        DialogoSaida(
            salvando = s.salvando,
            erro = s.erroSaida,
            onSalvar = vm::salvarESair,
            onDescartar = vm::descartarESair,
            onContinuar = vm::continuarEditando,
            descricaoSalvar = if (s.edicao?.topsisVigente != null) "Salvar avaliação concluída" else "Salvar como rascunho",
        )
    }
}

@Composable
private fun IndicadorEtapas(atual: Passo) {
    val indiceAtual = etapas.indexOfFirst { it.first == if (atual == Passo.PESOS) Passo.AHP else atual }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        etapas.forEachIndexed { i, (_, nome) ->
            Text(
                nome,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (i == indiceAtual) FontWeight.Bold else FontWeight.Normal,
                color = if (i == indiceAtual) Cores.primaria else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
