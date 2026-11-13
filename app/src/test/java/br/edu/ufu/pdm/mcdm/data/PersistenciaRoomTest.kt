package br.edu.ufu.pdm.mcdm.data

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.edu.ufu.pdm.mcdm.data.local.Codecs
import br.edu.ufu.pdm.mcdm.data.local.McdmDatabase
import br.edu.ufu.pdm.mcdm.data.mapper.AvaliacaoMapper
import br.edu.ufu.pdm.mcdm.data.repository.AvaliacaoRepositoryRoom
import br.edu.ufu.pdm.mcdm.data.repository.ProdutoPessoalRepositoryRoom
import br.edu.ufu.pdm.mcdm.domain.Fixtures
import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.EdicaoAvaliacao
import br.edu.ufu.pdm.mcdm.domain.model.EstadoSalvo
import br.edu.ufu.pdm.mcdm.domain.model.IdentificacaoProduto
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.model.Proveniencia
import br.edu.ufu.pdm.mcdm.domain.model.ResultadoTopsis
import br.edu.ufu.pdm.mcdm.domain.model.ValorAlternativa
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import br.edu.ufu.pdm.mcdm.domain.repository.AvaliacaoCorrompidaException
import br.edu.ufu.pdm.mcdm.domain.usecase.CalcularAhp
import br.edu.ufu.pdm.mcdm.domain.usecase.ProcessarAvaliacao
import br.edu.ufu.pdm.mcdm.domain.usecase.ResultadoCalculo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.Instant

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PersistenciaRoomTest {
    private lateinit var db: McdmDatabase
    private lateinit var repo: AvaliacaoRepositoryRoom

    private val uuidA = "11111111-1111-4111-8111-111111111111"
    private val uuidB = "22222222-2222-4222-8222-222222222222"

    @Before
    fun abrir() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), McdmDatabase::class.java)
            .addCallback(McdmDatabase.callback)
            .allowMainThreadQueries()
            .build()
        repo = AvaliacaoRepositoryRoom(db.avaliacaoDao())
    }

    @After
    fun fechar() = db.close()

    private fun concluida(): Avaliacao = runBlocking {
        val alts = listOf(
            Fixtures.alternativa("a", listOf(8000, 5000, 8, 16, 256)).copy(alternativaId = uuidA),
            Fixtures.alternativa("b", listOf(7000, 6000, 9, 20, 512), OrigemProduto.PESSOAL, 1).copy(alternativaId = uuidB),
        )
        var e = EdicaoAvaliacao.nova(Fixtures.avaliacao(alts).copy(julgamentos = Fixtures.julgamentosIguais()))
        e = e.comAhp((CalcularAhp(Dispatchers.Unconfined)(e.conteudo.criterios, e.conteudo.julgamentos) as ResultadoCalculo.Sucesso).valor)
        e = e.comTopsis((ProcessarAvaliacao(Dispatchers.Unconfined)(e.conteudo.criterios, e.conteudo.alternativas, e.ahpVigente) as ResultadoCalculo.Sucesso).valor)
        e.conteudoSalvavel().copy(salvaEm = Instant.ofEpochMilli(1_700_000_000_000))
    }

    @Test
    fun `round-trip preserva agregado concluido, centavos e doubles exatos`() = runBlocking {
        val a = concluida()
        // Ajuste efetivo com Long grande e original diferente.
        val ajustada = a.copy(
            topsis = null, ahp = a.ahp,
            alternativas = a.alternativas.map { alt ->
                if (alt.alternativaId != uuidA) alt else alt.copy(
                    valores = alt.valores + ("smartphones-preco" to ValorAlternativa(
                        ValorCanonico.Monetario(Long.MAX_VALUE), Proveniencia(fonte = "x", estimativa = null),
                        ValorCanonico.Monetario(1), Proveniencia.AJUSTE,
                    )),
                )
            },
        )
        repo.salvar(ajustada)
        assertEquals(ajustada, repo.obter(a.id))

        repo.salvar(a)
        val lida = repo.obter(a.id)!!
        assertEquals(a, lida)
        assertEquals(EstadoSalvo.CONCLUIDA_RANKING, lida.estado)
        assertEquals(1, repo.observarResumos().first().size)
    }

    @Test
    fun `sem diferenciacao persiste distancias sem C nem posicao`() = runBlocking {
        val a = concluida()
        val sd = a.copy(topsis = ResultadoTopsis.SemDiferenciacao(a.alternativas.associate { it.alternativaId to br.edu.ufu.pdm.mcdm.domain.model.Distancias(0.0, 0.0) }))
        repo.salvar(sd)
        assertEquals(EstadoSalvo.CONCLUIDA_SEM_DIFERENCIACAO, repo.obter(a.id)!!.estado)
    }

    @Test
    fun `falha no meio do salvamento reverte e preserva a versao anterior`() = runBlocking {
        val a = concluida()
        repo.salvar(a)
        // Duas alternativas com a mesma origem+ID violam UNIQUE depois que a raiz já foi gravada.
        val invalida = a.copy(
            titulo = "Não deveria persistir",
            topsis = null,
            alternativas = a.alternativas.map { it.copy(origemProduto = OrigemProduto.OFICIAL, idProdutoOrigem = "mesmo") },
        )
        try {
            db.avaliacaoDao().salvar(AvaliacaoMapper.paraLinhas(invalida))
            fail("esperava violação de UNIQUE")
        } catch (_: SQLiteConstraintException) {
        }
        assertEquals(a, repo.obter(a.id))
    }

    @Test
    fun `trigger rejeita par AHP invertido inserido diretamente`() = runBlocking {
        val a = concluida()
        repo.salvar(a)
        try {
            db.openHelper.writableDatabase.execSQL(
                "INSERT INTO julgamento_ahp VALUES (?, 'smartphones-preco', 'smartphones-autonomia', 3, 'MENOR_PREFERIDO')",
                arrayOf(a.id),
            )
            fail("esperava abort do trigger")
        } catch (_: SQLiteException) {
        }
        try {
            db.openHelper.writableDatabase.execSQL(
                "INSERT INTO julgamento_ahp VALUES (?, 'smartphones-autonomia', 'smartphones-preco', 1, 'MENOR_PREFERIDO')",
                arrayOf(a.id),
            )
            fail("esperava abort por direção incoerente")
        } catch (_: SQLiteException) {
        }
    }

    @Test
    fun `exclusao remove dependentes em cascata`() = runBlocking {
        val a = concluida()
        repo.salvar(a)
        repo.excluir(a.id)
        assertNull(repo.obter(a.id))
        val c = db.openHelper.readableDatabase.query("SELECT (SELECT COUNT(*) FROM valor_alternativa) + (SELECT COUNT(*) FROM julgamento_ahp) + (SELECT COUNT(*) FROM criterio_preservado)")
        c.moveToFirst()
        assertEquals(0, c.getInt(0))
        c.close()
    }

    @Test
    fun `leitura inconsistente e sinalizada, nao reparada`() = runBlocking {
        val a = concluida()
        repo.salvar(a)
        db.openHelper.writableDatabase.execSQL("UPDATE alternativa_avaliacao SET coeficiente = NULL WHERE alternativaId = ?", arrayOf(uuidA))
        try {
            repo.obter(a.id)
            fail("esperava avaliação corrompida")
        } catch (_: AvaliacaoCorrompidaException) {
        }
    }

    @Test
    fun `produto pessoal faz round-trip do payload versionado`() = runBlocking {
        val r = ProdutoPessoalRepositoryRoom(db.produtoPessoalDao())
        val p = ProdutoPessoal(
            id = "33333333-3333-4333-8333-333333333333", categoriaId = "smartphones-premium", categoriaNome = "Smartphones Premium",
            revisaoDefinicao = "r1", identificacao = IdentificacaoProduto("Meu", fabricante = "X"),
            definicoes = Fixtures.criteriosSmartphone,
            valores = mapOf("smartphones-preco" to ValorCanonico.Monetario(123456789012L), "smartphones-autonomia" to ValorCanonico.Numero(0.1 + 0.2)),
            referencias = mapOf("smartphones-preco" to Proveniencia(fonte = "loja", dataReferencia = "2026-11-01", estimativa = false)),
        )
        r.salvar(p)
        assertEquals(p, r.obter(p.id))
        assertEquals(listOf(p), r.observarPorCategoria("smartphones-premium").first())
    }

    @Test
    fun `codec rejeita envelope contraditorio e formato desconhecido`() {
        assertTrue(runCatching { Codecs.deColunas("MONETARIO_BRL", null, 1.0) }.isFailure)
        assertTrue(runCatching { Codecs.deColunas(null, 5, null) }.isFailure)
        assertTrue(runCatching { Codecs.payloadDeJson("{}", 2) }.isFailure)
        assertEquals(ValorCanonico.Numero(0.30000000000000004), Codecs.deColunas("NUMERO", null, 0.1 + 0.2))
    }
}
