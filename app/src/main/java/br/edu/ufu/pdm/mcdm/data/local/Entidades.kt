package br.edu.ufu.pdm.mcdm.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

// Seis tabelas da DEC-038 (contrato 01). FKs só internas à avaliação, com cascata.

@Entity(tableName = "produto_pessoal", indices = [Index("categoriaId")])
data class ProdutoPessoalEntity(
    @androidx.room.PrimaryKey val produtoId: String,
    val categoriaId: String,
    val categoriaNome: String,
    val revisaoDefinicao: String?,
    val nome: String,
    val fabricante: String?,
    val modelo: String?,
    val versao: String?,
    val ano: Int?,
    val payloadJson: String,
    val formatoPayload: Int,
)

@Entity(tableName = "avaliacao", indices = [Index("salvaEm")])
data class AvaliacaoEntity(
    @androidx.room.PrimaryKey val avaliacaoId: String,
    val categoriaId: String,
    val categoriaNome: String,
    val revisaoDefinicao: String?,
    val titulo: String?,
    val estadoSalvo: String,
    /** AUSENTE, RANKING ou SEM_DIFERENCIACAO. */
    val tipoTopsis: String,
    val criadaEm: Long,
    val salvaEm: Long,
    // Bloco AHP indivisível: lambdaMax/ic/ri presentes juntos; rc pode ser NULL quando RI = 0.
    val lambdaMax: Double?,
    val ic: Double?,
    val ri: Double?,
    val rc: Double?,
)

@Entity(
    tableName = "criterio_preservado",
    primaryKeys = ["avaliacaoId", "criterioId"],
    indices = [Index(value = ["avaliacaoId", "ordem"], unique = true)],
    foreignKeys = [
        ForeignKey(entity = AvaliacaoEntity::class, parentColumns = ["avaliacaoId"], childColumns = ["avaliacaoId"], onDelete = ForeignKey.CASCADE),
    ],
)
data class CriterioPreservadoEntity(
    val avaliacaoId: String,
    val criterioId: String,
    val nome: String,
    val ordem: Int,
    val unidade: String,
    val natureza: String,
    val validacaoJson: String,
    val descricao: String?,
    val pesoAhp: Double?,
)

@Entity(
    tableName = "alternativa_avaliacao",
    primaryKeys = ["avaliacaoId", "alternativaId"],
    indices = [Index(value = ["avaliacaoId", "origemProduto", "idProdutoOrigem"], unique = true)],
    foreignKeys = [
        ForeignKey(entity = AvaliacaoEntity::class, parentColumns = ["avaliacaoId"], childColumns = ["avaliacaoId"], onDelete = ForeignKey.CASCADE),
    ],
)
data class AlternativaEntity(
    val avaliacaoId: String,
    val alternativaId: String,
    val origemProduto: String,
    val idProdutoOrigem: String,
    val nome: String,
    val fabricante: String?,
    val modelo: String?,
    val versao: String?,
    val ano: Int?,
    val ordem: Int,
    val atualizadoNaOrigemEm: Long?,
    val dMais: Double?,
    val dMenos: Double?,
    val coeficiente: Double?,
    val posicao: Int?,
)

@Entity(
    tableName = "valor_alternativa",
    primaryKeys = ["avaliacaoId", "alternativaId", "criterioId"],
    indices = [Index(value = ["avaliacaoId", "criterioId"])],
    foreignKeys = [
        ForeignKey(
            entity = AlternativaEntity::class,
            parentColumns = ["avaliacaoId", "alternativaId"], childColumns = ["avaliacaoId", "alternativaId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = CriterioPreservadoEntity::class,
            parentColumns = ["avaliacaoId", "criterioId"], childColumns = ["avaliacaoId", "criterioId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class ValorAlternativaEntity(
    val avaliacaoId: String,
    val alternativaId: String,
    val criterioId: String,
    @ColumnInfo(name = "origem_tipo") val origemTipo: String?,
    @ColumnInfo(name = "origem_centavos") val origemCentavos: Long?,
    @ColumnInfo(name = "origem_numero") val origemNumero: Double?,
    @ColumnInfo(name = "origem_proveniencia") val origemProveniencia: String?,
    @ColumnInfo(name = "efetivo_tipo") val efetivoTipo: String?,
    @ColumnInfo(name = "efetivo_centavos") val efetivoCentavos: Long?,
    @ColumnInfo(name = "efetivo_numero") val efetivoNumero: Double?,
    @ColumnInfo(name = "efetivo_proveniencia") val efetivoProveniencia: String?,
    val entradaPendenteEfetiva: String?,
)

@Entity(
    tableName = "julgamento_ahp",
    primaryKeys = ["avaliacaoId", "criterioMenorId", "criterioMaiorId"],
    indices = [Index(value = ["avaliacaoId", "criterioMaiorId"])],
    foreignKeys = [
        ForeignKey(
            entity = CriterioPreservadoEntity::class,
            parentColumns = ["avaliacaoId", "criterioId"], childColumns = ["avaliacaoId", "criterioMenorId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = CriterioPreservadoEntity::class,
            parentColumns = ["avaliacaoId", "criterioId"], childColumns = ["avaliacaoId", "criterioMaiorId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class JulgamentoEntity(
    val avaliacaoId: String,
    val criterioMenorId: String,
    val criterioMaiorId: String,
    val intensidade: Int,
    val direcao: String,
)

/** Linha da lista T05. */
data class ResumoRow(
    val avaliacaoId: String,
    val titulo: String?,
    val categoriaNome: String,
    val estadoSalvo: String,
    val salvaEm: Long,
    val quantidade: Int,
)
