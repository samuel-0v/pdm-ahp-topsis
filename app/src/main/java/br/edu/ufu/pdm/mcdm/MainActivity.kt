package br.edu.ufu.pdm.mcdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.edu.ufu.pdm.mcdm.presentation.abertura.Abertura
import br.edu.ufu.pdm.mcdm.presentation.navigation.AppNavHost
import br.edu.ufu.pdm.mcdm.presentation.theme.TemaMcdm

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as McdmApplication).container
        setContent {
            TemaMcdm {
                // A abertura (T00) roda uma vez por execução; não reaparece após rotação ou retorno.
                var aberturaConcluida by rememberSaveable { mutableStateOf(false) }
                Box(Modifier.fillMaxSize()) {
                    AppNavHost(container)
                    if (!aberturaConcluida) Abertura(onConcluir = { aberturaConcluida = true })
                }
            }
        }
    }
}
