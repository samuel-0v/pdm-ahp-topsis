package br.edu.ufu.pdm.mcdm.data.remote

import android.util.Log
import br.edu.ufu.pdm.mcdm.domain.model.Categoria
import br.edu.ufu.pdm.mcdm.domain.model.EstadoConsulta
import br.edu.ufu.pdm.mcdm.domain.model.OrigemDados
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoOficial
import br.edu.ufu.pdm.mcdm.domain.repository.CatalogoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.Clock
import java.time.Instant

/**
 * Catálogo oficial somente leitura (DEC-009/010/020). Listeners com metadados incluídos para que a
 * mudança cache → servidor atualize a origem exibida mesmo sem mudança de conteúdo.
 * `fromCache` identifica a origem do evento, não completude nem atualidade.
 */
class CatalogoRepositoryFirestore(
    private val firestore: FirebaseFirestore,
    private val relogio: Clock,
) : CatalogoRepository {

    private sealed interface Evento {
        data class Snapshot(val s: QuerySnapshot) : Evento
        data class Erro(val e: Exception) : Evento
    }

    private fun observar(query: Query): Flow<Evento?> = callbackFlow {
        trySend(null)
        val registro = query.addSnapshotListener(MetadataChanges.INCLUDE) { s, e ->
            when {
                e != null -> trySend(Evento.Erro(e))
                s != null -> trySend(Evento.Snapshot(s))
            }
        }
        awaitClose { registro.remove() }
    }

    private var confirmacaoCategorias: Instant? = null
    private val confirmacaoProdutos = mutableMapOf<String, Instant>()

    private fun interpretarCategorias(ev: Evento?): EstadoConsulta<List<Categoria>> = when (ev) {
        null -> EstadoConsulta.Carregando
        is Evento.Erro -> erro(ev.e)
        is Evento.Snapshot -> {
            if (!ev.s.metadata.isFromCache) confirmacaoCategorias = Instant.now(relogio)
            var invalidos = 0
            val lista = ev.s.documents.mapNotNull { doc ->
                try {
                    FirestoreMapper.categoria(doc.id, doc.data.orEmpty())
                } catch (e: DocumentoInvalidoException) {
                    Log.w(TAG, "Categoria ${doc.id} ignorada: ${e.message}")
                    invalidos++
                    null
                }
            }.sortedBy { it.nome }
            EstadoConsulta.Dados(lista, origem(ev.s), confirmacaoCategorias, invalidos)
        }
    }

    override fun observarCategorias(): Flow<EstadoConsulta<List<Categoria>>> =
        observarCategoriasInterno()

    private fun observarCategoriasInterno(): Flow<EstadoConsulta<List<Categoria>>> =
        kotlinx.coroutines.flow.flow {
            observar(firestore.collection(CATEGORIAS)).collect { emit(interpretarCategorias(it)) }
        }.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observarProdutos(categoriaId: String): Flow<EstadoConsulta<List<ProdutoOficial>>> =
        combine(
            observarCategoriasInterno(),
            observar(firestore.collection(PRODUTOS).whereEqualTo("categoriaId", categoriaId)),
        ) { cats, ev ->
            when (ev) {
                null -> EstadoConsulta.Carregando
                is Evento.Erro -> erro(ev.e)
                is Evento.Snapshot -> {
                    if (!ev.s.metadata.isFromCache) confirmacaoProdutos[categoriaId] = Instant.now(relogio)
                    val categorias = (cats as? EstadoConsulta.Dados)?.dados.orEmpty().associateBy { it.id }
                    var invalidos = 0
                    val lista = ev.s.documents.mapNotNull { doc ->
                        try {
                            FirestoreMapper.produto(doc.id, doc.data.orEmpty(), categorias)
                        } catch (e: DocumentoInvalidoException) {
                            Log.w(TAG, "Produto ${doc.id} ignorado: ${e.message}")
                            invalidos++
                            null
                        }
                    }.sortedBy { it.identificacao.nome }
                    EstadoConsulta.Dados(lista, origem(ev.s), confirmacaoProdutos[categoriaId], invalidos)
                }
            }
        }.distinctUntilChanged()

    private fun origem(s: QuerySnapshot) = if (s.metadata.isFromCache) OrigemDados.CACHE else OrigemDados.SERVIDOR

    private fun <T> erro(e: Exception): EstadoConsulta<T> =
        if ((e as? FirebaseFirestoreException)?.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
            EstadoConsulta.PermissaoNegada
        } else {
            EstadoConsulta.Indisponivel(e.message ?: "Catálogo indisponível.")
        }

    companion object {
        private const val TAG = "CatalogoFirestore"
        const val CATEGORIAS = "categorias"
        const val PRODUTOS = "produtos"
    }
}
