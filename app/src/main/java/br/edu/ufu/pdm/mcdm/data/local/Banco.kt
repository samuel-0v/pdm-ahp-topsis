package br.edu.ufu.pdm.mcdm.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoPessoalDao {
    @Query("SELECT * FROM produto_pessoal WHERE categoriaId = :categoriaId ORDER BY nome COLLATE NOCASE, produtoId")
    fun observarPorCategoria(categoriaId: String): Flow<List<ProdutoPessoalEntity>>

    @Query("SELECT * FROM produto_pessoal WHERE produtoId = :id")
    fun observar(id: String): Flow<ProdutoPessoalEntity?>

    @Query("SELECT * FROM produto_pessoal WHERE produtoId = :id")
    suspend fun obter(id: String): ProdutoPessoalEntity?

    @Upsert
    suspend fun salvar(e: ProdutoPessoalEntity)

    @Query("DELETE FROM produto_pessoal WHERE produtoId = :id")
    suspend fun excluir(id: String): Int
}

/** Conjunto de linhas de uma avaliação lido/gravado numa única transação. */
data class LinhasAvaliacao(
    val avaliacao: AvaliacaoEntity,
    val criterios: List<CriterioPreservadoEntity>,
    val alternativas: List<AlternativaEntity>,
    val valores: List<ValorAlternativaEntity>,
    val julgamentos: List<JulgamentoEntity>,
)

@Dao
abstract class AvaliacaoDao {
    @Query(
        """
        SELECT a.avaliacaoId, a.titulo, a.categoriaNome, a.estadoSalvo, a.salvaEm,
               (SELECT COUNT(*) FROM alternativa_avaliacao x WHERE x.avaliacaoId = a.avaliacaoId) AS quantidade
        FROM avaliacao a ORDER BY a.salvaEm DESC, a.avaliacaoId
        """,
    )
    abstract fun observarResumos(): Flow<List<ResumoRow>>

    @Query("SELECT * FROM avaliacao WHERE avaliacaoId = :id")
    protected abstract suspend fun raiz(id: String): AvaliacaoEntity?

    @Query("SELECT * FROM criterio_preservado WHERE avaliacaoId = :id ORDER BY ordem")
    protected abstract suspend fun criterios(id: String): List<CriterioPreservadoEntity>

    @Query("SELECT * FROM alternativa_avaliacao WHERE avaliacaoId = :id ORDER BY ordem")
    protected abstract suspend fun alternativas(id: String): List<AlternativaEntity>

    @Query("SELECT * FROM valor_alternativa WHERE avaliacaoId = :id")
    protected abstract suspend fun valores(id: String): List<ValorAlternativaEntity>

    @Query("SELECT * FROM julgamento_ahp WHERE avaliacaoId = :id")
    protected abstract suspend fun julgamentos(id: String): List<JulgamentoEntity>

    /** Atualiza a raiz sem REPLACE: não dispara cascatas implícitas (contrato 06). */
    @Upsert
    protected abstract suspend fun gravarRaiz(e: AvaliacaoEntity)

    @Query("DELETE FROM criterio_preservado WHERE avaliacaoId = :id")
    protected abstract suspend fun limparCriterios(id: String)

    @Query("DELETE FROM alternativa_avaliacao WHERE avaliacaoId = :id")
    protected abstract suspend fun limparAlternativas(id: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    protected abstract suspend fun inserirCriterios(l: List<CriterioPreservadoEntity>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    protected abstract suspend fun inserirAlternativas(l: List<AlternativaEntity>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    protected abstract suspend fun inserirValores(l: List<ValorAlternativaEntity>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    protected abstract suspend fun inserirJulgamentos(l: List<JulgamentoEntity>)

    @Query("DELETE FROM avaliacao WHERE avaliacaoId = :id")
    abstract suspend fun excluir(id: String): Int

    /** Leitura consistente das seis tabelas. */
    @Transaction
    open suspend fun carregar(id: String): LinhasAvaliacao? {
        val r = raiz(id) ?: return null
        return LinhasAvaliacao(r, criterios(id), alternativas(id), valores(id), julgamentos(id))
    }

    /** Salvamento atômico: raiz, filhos e resultados; qualquer falha reverte tudo. */
    @Transaction
    open suspend fun salvar(l: LinhasAvaliacao) {
        val id = l.avaliacao.avaliacaoId
        gravarRaiz(l.avaliacao)
        limparAlternativas(id) // remove valores em cascata
        limparCriterios(id) // remove julgamentos em cascata
        inserirCriterios(l.criterios)
        inserirAlternativas(l.alternativas)
        inserirValores(l.valores)
        inserirJulgamentos(l.julgamentos)
    }
}

@Database(
    entities = [
        ProdutoPessoalEntity::class,
        AvaliacaoEntity::class,
        CriterioPreservadoEntity::class,
        AlternativaEntity::class,
        ValorAlternativaEntity::class,
        JulgamentoEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class McdmDatabase : RoomDatabase() {
    abstract fun produtoPessoalDao(): ProdutoPessoalDao
    abstract fun avaliacaoDao(): AvaliacaoDao

    companion object {
        const val NOME = "mcdm.db"

        /**
         * Defesa física do par canônico (DEC-038): Room não declara CHECK, então triggers
         * BEFORE INSERT/UPDATE abortam par invertido/igual, intensidade fora de 1–9 e direção
         * incoerente. A comparação TEXT usa a colação BINARY (bytes UTF-8), igual ao normalizador.
         */
        val TRIGGERS = listOf("INSERT", "UPDATE").map { op ->
            """
            CREATE TRIGGER IF NOT EXISTS julgamento_canonico_${op.lowercase()}
            BEFORE $op ON julgamento_ahp
            WHEN NEW.criterioMenorId >= NEW.criterioMaiorId
              OR NEW.intensidade < 1 OR NEW.intensidade > 9
              OR NEW.direcao NOT IN ('IGUAL', 'MENOR_PREFERIDO', 'MAIOR_PREFERIDO')
              OR ((NEW.intensidade = 1) <> (NEW.direcao = 'IGUAL'))
            BEGIN
              SELECT RAISE(ABORT, 'julgamento_ahp fora da forma canonica');
            END
            """.trimIndent()
        }

        val callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                TRIGGERS.forEach(db::execSQL)
            }
        }

        fun criar(context: Context): McdmDatabase =
            Room.databaseBuilder(context, McdmDatabase::class.java, NOME)
                .addCallback(callback)
                .build()
    }
}
