package br.edu.ufu.pdm.mcdm.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Tokens do design system da Etapa 5 v0.5. */
object Cores {
    val primaria = Color(0xFF006A62)
    val primariaSuave = Color(0xFFD8F2ED)
    val tinta = Color(0xFF142C2F)
    val suave = Color(0xFF4B6062)
    val superficie = Color(0xFFFFFFFF)
    val fundo = Color(0xFFF4F7F6)
    val linha = Color(0xFF6D8181)
    val sucesso = Color(0xFF205E42)
    val sucessoSuave = Color(0xFFE1F3E8)
    val erro = Color(0xFFA52932)
    val erroSuave = Color(0xFFFFECEE)
    val aviso = Color(0xFF775214)
    val avisoSuave = Color(0xFFFFF2D8)
    val pessoal = Color(0xFF5B478F)
    val pessoalSuave = Color(0xFFF0EAFB)
    val aberturaFundo = Color(0xFF142C2F)
    val marcaClara = Color(0xFF9BDAC3)
    val marcaMedia = Color(0xFFB9D8CD)
}

private val esquema = lightColorScheme(
    primary = Cores.primaria,
    onPrimary = Color.White,
    primaryContainer = Cores.primariaSuave,
    onPrimaryContainer = Cores.tinta,
    secondary = Cores.suave,
    onSecondary = Color.White,
    secondaryContainer = Cores.primariaSuave,
    onSecondaryContainer = Cores.tinta,
    tertiary = Cores.pessoal,
    tertiaryContainer = Cores.pessoalSuave,
    onTertiaryContainer = Cores.pessoal,
    background = Cores.fundo,
    onBackground = Cores.tinta,
    surface = Cores.superficie,
    onSurface = Cores.tinta,
    surfaceVariant = Cores.fundo,
    onSurfaceVariant = Cores.suave,
    surfaceContainer = Cores.superficie,
    surfaceContainerLow = Cores.superficie,
    surfaceContainerHigh = Cores.superficie,
    outline = Cores.linha,
    outlineVariant = Color(0xFFC7D3D1),
    error = Cores.erro,
    onError = Color.White,
    errorContainer = Cores.erroSuave,
    onErrorContainer = Cores.erro,
)

private val tipografia = Typography(
    headlineMedium = TextStyle(fontSize = 30.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold),
    headlineSmall = TextStyle(fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.Medium),
    titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 18.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
    labelSmall = TextStyle(fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
)

private val formas = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

@Composable
fun TemaMcdm(conteudo: @Composable () -> Unit) {
    MaterialTheme(colorScheme = esquema, typography = tipografia, shapes = formas, content = conteudo)
}
