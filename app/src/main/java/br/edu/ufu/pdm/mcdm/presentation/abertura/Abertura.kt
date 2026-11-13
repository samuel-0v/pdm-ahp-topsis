package br.edu.ufu.pdm.mcdm.presentation.abertura

import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.edu.ufu.pdm.mcdm.presentation.theme.Cores
import kotlinx.coroutines.launch

/**
 * T00 — abertura animada (DEC-049): desenho das três barras e revelação da identificação em
 * 520 ms, dissolução de 160 ms. Não espera rede; pode ser pulada; com animações desativadas no
 * sistema segue direto ao conteúdo. Exibida uma vez por execução (não reaparece em retornos).
 */
@Composable
fun Abertura(onConcluir: () -> Unit) {
    val context = LocalContext.current
    val semMovimento = remember {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
    val progresso = remember { Animatable(if (semMovimento) 1f else 0f) }
    val opacidade = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        if (semMovimento) {
            onConcluir(); return@LaunchedEffect
        }
        progresso.animateTo(1f, tween(520, easing = FastOutSlowInEasing))
        opacidade.animateTo(0f, tween(160))
        onConcluir()
    }
    val escopo = androidx.compose.runtime.rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
            .alpha(opacidade.value)
            .background(Cores.aberturaFundo)
            .semantics { contentDescription = "Abertura PDM AHP-TOPSIS" },
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Canvas(Modifier.size(112.dp)) {
                val p = progresso.value
                val largura = size.width * 0.18f
                val alturas = listOf(0.36f, 0.6f, 0.86f)
                val cores = listOf(Cores.marcaMedia, Cores.marcaClara, Color.White)
                alturas.forEachIndexed { i, h ->
                    val atraso = i * 0.18f
                    val local = ((p - atraso) / (1f - atraso)).coerceIn(0f, 1f)
                    val alt = size.height * h * local
                    drawRoundRect(
                        color = cores[i],
                        topLeft = Offset(size.width * (0.2f + i * 0.24f), size.height * 0.92f - alt),
                        size = Size(largura, alt),
                        cornerRadius = CornerRadius(largura / 3),
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("PDM AHP-TOPSIS", color = Color.White, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.alpha(progresso.value))
            Spacer(Modifier.height(8.dp))
            Text(
                "Comparação multicritério de produtos",
                color = Cores.marcaMedia,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alpha(progresso.value),
            )
        }
        TextButton(
            onClick = { escopo.launch { opacidade.snapTo(0f); onConcluir() } },
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(24.dp),
        ) { Text("Ir ao catálogo", color = Color.White) }
    }
}
