package br.edu.ufu.pdm.mcdm.di

import android.content.Context
import android.util.Log
import br.edu.ufu.pdm.mcdm.BuildConfig
import br.edu.ufu.pdm.mcdm.data.exportacao.GeradorRelatorioPdfAndroid
import br.edu.ufu.pdm.mcdm.data.local.McdmDatabase
import br.edu.ufu.pdm.mcdm.data.remote.CatalogoRepositoryFirestore
import br.edu.ufu.pdm.mcdm.data.repository.AvaliacaoRepositoryRoom
import br.edu.ufu.pdm.mcdm.data.repository.ProdutoPessoalRepositoryRoom
import br.edu.ufu.pdm.mcdm.domain.repository.AvaliacaoRepository
import br.edu.ufu.pdm.mcdm.domain.repository.CatalogoRepository
import br.edu.ufu.pdm.mcdm.domain.repository.ProdutoPessoalRepository
import br.edu.ufu.pdm.mcdm.domain.usecase.CalcularAhp
import br.edu.ufu.pdm.mcdm.domain.usecase.ExportarAvaliacao
import br.edu.ufu.pdm.mcdm.domain.usecase.PrepararAlternativa
import br.edu.ufu.pdm.mcdm.domain.usecase.ProcessarAvaliacao
import br.edu.ufu.pdm.mcdm.domain.usecase.SalvarAvaliacao
import br.edu.ufu.pdm.mcdm.domain.usecase.SalvarProdutoPessoal
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.Clock

/**
 * Composição manual das dependências (DEC-044). Criado uma vez pela Application; UI e Domain
 * não o procuram globalmente — recebem dependências pelos construtores/fábricas.
 */
class AppContainer(context: Context) {
    private val app = context.applicationContext

    val relogio: Clock = Clock.systemDefaultZone()
    val dispatcherCalculo: CoroutineDispatcher = Dispatchers.Default
    val dispatcherIo: CoroutineDispatcher = Dispatchers.IO

    private val banco by lazy { McdmDatabase.criar(app) }

    private val firestore: FirebaseFirestore by lazy {
        val firebaseApp = FirebaseApp.getApps(app).firstOrNull() ?: FirebaseApp.initializeApp(
            app,
            FirebaseOptions.Builder()
                .setProjectId(BuildConfig.FIREBASE_PROJECT_ID)
                .setApplicationId(BuildConfig.FIREBASE_APPLICATION_ID)
                .setApiKey(BuildConfig.FIREBASE_API_KEY)
                .build(),
        )
        FirebaseFirestore.getInstance(firebaseApp).apply {
            // Projeto demo do Local Emulator Suite (firebase/README.md).
            useEmulator(BuildConfig.FIRESTORE_EMULATOR_HOST, BuildConfig.FIRESTORE_EMULATOR_PORT)
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
                .build()
            Log.i("AppContainer", "Firestore -> ${BuildConfig.FIRESTORE_EMULATOR_HOST}:${BuildConfig.FIRESTORE_EMULATOR_PORT}")
        }
    }

    val catalogo: CatalogoRepository by lazy { CatalogoRepositoryFirestore(firestore, relogio) }
    val produtosPessoais: ProdutoPessoalRepository by lazy { ProdutoPessoalRepositoryRoom(banco.produtoPessoalDao()) }
    val avaliacoes: AvaliacaoRepository by lazy { AvaliacaoRepositoryRoom(banco.avaliacaoDao()) }

    val calcularAhp by lazy { CalcularAhp(dispatcherCalculo) }
    val processarAvaliacao by lazy { ProcessarAvaliacao(dispatcherCalculo) }
    val salvarAvaliacao by lazy { SalvarAvaliacao(avaliacoes, relogio) }
    val salvarProdutoPessoal by lazy { SalvarProdutoPessoal(produtosPessoais) }
    val prepararAlternativa by lazy { PrepararAlternativa() }
    val exportarAvaliacao by lazy { ExportarAvaliacao(avaliacoes, GeradorRelatorioPdfAndroid(app, dispatcherIo), relogio) }
}
