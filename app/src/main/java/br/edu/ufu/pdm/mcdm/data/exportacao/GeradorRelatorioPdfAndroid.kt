package br.edu.ufu.pdm.mcdm.data.exportacao

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import br.edu.ufu.pdm.mcdm.domain.exportacao.ConteudoRelatorio
import br.edu.ufu.pdm.mcdm.domain.exportacao.GeradorRelatorioPdf
import br.edu.ufu.pdm.mcdm.domain.exportacao.RelatorioGerado
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * PDF local (DEC-032/048): desenha o conteúdo já preparado pelo Domain; não consulta banco
 * nem catálogo. Arquivo em cache privado, compartilhado depois por URI temporária.
 *
 * Layout: faixa de título, quadro de resultado, tabelas (critérios/pesos, ranking, matriz de
 * valores) com cabeçalho repetido a cada página e rodapé "página N de M". O documento é
 * desenhado duas vezes: a primeira só conta as páginas.
 */
class GeradorRelatorioPdfAndroid(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : GeradorRelatorioPdf {

    private val dataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).withZone(ZoneId.systemDefault())

    override suspend fun gerar(conteudo: ConteudoRelatorio): RelatorioGerado = withContext(dispatcher) {
        val total = PdfDocument().let { rascunho ->
            try { desenhar(rascunho, conteudo, null) } finally { rascunho.close() }
        }
        val pdf = PdfDocument()
        try {
            desenhar(pdf, conteudo, total)
            val pasta = File(context.cacheDir, "relatorios").apply { mkdirs() }
            pasta.listFiles()?.filter { System.currentTimeMillis() - it.lastModified() > 86_400_000 }?.forEach { it.delete() }
            val nome = "avaliacao-" + conteudo.titulo.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-').take(40) +
                "-" + conteudo.geradoEm.toEpochMilli() + ".pdf"
            val arquivo = File(pasta, nome)
            arquivo.outputStream().use { pdf.writeTo(it) }
            RelatorioGerado(arquivo.absolutePath, nome)
        } finally {
            pdf.close()
        }
    }

    /** Desenha o relatório inteiro e devolve o número de páginas. */
    private fun desenhar(pdf: PdfDocument, c: ConteudoRelatorio, totalPaginas: Int?): Int {
        val d = Desenho(pdf, c.titulo, "Gerado em ${dataHora.format(c.geradoEm)}", totalPaginas)
        d.capa(c.titulo, c.categoria.takeIf { it != c.titulo })
        d.texto(
            listOfNotNull("PDF gerado em ${dataHora.format(c.geradoEm)}", c.salvaEm?.let { "versão salva em ${dataHora.format(it)}" }).joinToString(" · ") +
                ". Estas datas não são datas de referência dos dados.",
            tamanho = 8.5f, cor = CINZA,
        )
        d.espaco(10f)

        d.quadroResultado(
            rotulo = "RESULTADO",
            destaque = c.vencedora?.let { "1º lugar: $it" },
            texto = c.situacao,
        )

        d.secao("Critérios e pesos (AHP)")
        val maiorPeso = c.criterios.maxOfOrNull { it.pesoValor }?.takeIf { it > 0 } ?: 1.0
        d.tabela(
            listOf(Coluna("Critério", 3.2f), Coluna("Unidade", 1.5f), Coluna("Natureza", 1.3f), Coluna("Peso", 2.4f)),
            c.criterios.map {
                listOf(Celula(it.nome, negrito = true), Celula(it.unidade), Celula(it.natureza), Celula(it.peso, barra = (it.pesoValor / maiorPeso).toFloat()))
            },
        )
        d.espaco(8f)
        d.indicadores(c.indicadoresAhp)
        d.texto("Julgamentos consistentes quando RC ≤ 0,10.", tamanho = 8f, cor = CINZA)

        d.secao("Ranking TOPSIS")
        d.tabela(
            listOf(Coluna("Pos.", 0.9f), Coluna("Alternativa", 3.0f), Coluna("Origem", 1.1f), Coluna("C*", 2.2f), Coluna("d+", 1.2f), Coluna("d−", 1.2f)),
            c.alternativas.map {
                listOf(
                    Celula(it.posicao, negrito = true),
                    Celula(it.nome, negrito = true),
                    Celula(it.origem),
                    Celula(it.coeficiente, barra = it.coeficienteValor?.toFloat()),
                    Celula(it.dMais),
                    Celula(it.dMenos),
                )
            },
            fundoLinha = { i -> if (c.vencedora != null && i == 0) DESTAQUE else null },
        )
        d.texto("C* = d− / (d+ + d−): quanto mais próximo de 1, mais perto da solução ideal.", tamanho = 8f, cor = CINZA)

        d.secao("Valores utilizados")
        val criterios = c.alternativas.firstOrNull()?.valores?.map { it.criterio }.orEmpty()
        d.tabela(
            listOf(Coluna("Alternativa", 1.6f)) + criterios.map { Coluna(it, 1f) },
            c.alternativas.map { a ->
                listOf(Celula(a.nome, negrito = true)) + a.valores.map { v ->
                    Celula(if (v.ajustado) v.texto + " *" else v.texto, fundo = if (v.ajustado) AJUSTADO else null)
                }
            },
            fonte = 8f,
        )
        if (c.alternativas.any { a -> a.valores.any { it.ajustado } }) {
            d.texto("* Valor ajustado nesta avaliação; o catálogo de origem não foi alterado.", tamanho = 8f, cor = CINZA)
        }

        d.secao("Método", reservar = d.alturaTexto(c.metodo, 8.5f))
        d.texto(c.metodo, tamanho = 8.5f, cor = CINZA)
        return d.finalizar()
    }

    private class Coluna(val titulo: String, val peso: Float)

    /** [barra] em 0..1 desenha uma barra proporcional sob o texto. */
    private class Celula(val texto: String, val negrito: Boolean = false, val barra: Float? = null, val fundo: Int? = null)

    private class Desenho(
        private val pdf: PdfDocument,
        private val tituloCurto: String,
        private val textoRodape: String,
        private val totalPaginas: Int?,
    ) {
        private var numero = 0
        private var pagina: PdfDocument.Page? = null
        private var y = 0f
        private val largura = LARGURA - 2 * MARGEM
        private val limite = ALTURA - MARGEM - 18f
        private val preenchimento = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
        private val traco = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeWidth = 0.6f; color = BORDA }

        private val canvas: Canvas get() = (pagina ?: novaPagina()).canvas

        private fun novaPagina(): PdfDocument.Page {
            pagina?.let { rodape(it); pdf.finishPage(it) }
            numero++
            val p = pdf.startPage(PdfDocument.PageInfo.Builder(LARGURA.toInt(), ALTURA.toInt(), numero).create())
            pagina = p
            y = MARGEM
            if (numero > 1) {
                val tp = paint(8f, cor = CINZA)
                p.canvas.drawText("Relatório AHP-TOPSIS · ${tituloCurto.take(70)}", MARGEM, y + 8f, tp)
                p.canvas.drawLine(MARGEM, y + 14f, LARGURA - MARGEM, y + 14f, traco)
                y += 26f
            }
            return p
        }

        private fun rodape(p: PdfDocument.Page) {
            val tp = paint(8f, cor = CINZA)
            val base = ALTURA - 26f
            p.canvas.drawLine(MARGEM, base - 12f, LARGURA - MARGEM, base - 12f, traco)
            p.canvas.drawText("PDM AHP-TOPSIS · $textoRodape", MARGEM, base, tp)
            val pag = "Página $numero" + (totalPaginas?.let { " de $it" } ?: "")
            p.canvas.drawText(pag, LARGURA - MARGEM - tp.measureText(pag), base, tp)
        }

        private fun garantir(altura: Float) {
            if (pagina == null || y + altura > limite) novaPagina()
        }

        /** [categoria] null quando repete o título (avaliação sem título próprio). */
        fun capa(titulo: String, categoria: String?) {
            garantir(0f)
            val tituloL = layout(titulo, paint(17f, negrito = true, cor = Color.WHITE), largura - 32f)
            val altura = (if (categoria != null) 56f else 42f) + tituloL.height
            preenchimento.color = PRIMARIA
            canvas.drawRoundRect(RectF(MARGEM, y, LARGURA - MARGEM, y + altura), 8f, 8f, preenchimento)
            canvas.drawText("RELATÓRIO DE AVALIAÇÃO AHP-TOPSIS", MARGEM + 16f, y + 22f, paint(8.5f, negrito = true, cor = CLARO))
            desenharLayout(tituloL, MARGEM + 16f, y + 30f)
            categoria?.let { canvas.drawText("Categoria: $it", MARGEM + 16f, y + 30f + tituloL.height + 15f, paint(10f, cor = CLARO)) }
            y += altura + 8f
        }

        fun quadroResultado(rotulo: String, destaque: String?, texto: String) {
            val interno = largura - 28f
            val destaqueL = destaque?.let { layout(it, paint(14f, negrito = true, cor = PRIMARIA_ESCURA), interno) }
            val textoL = layout(texto, paint(10f, cor = TINTA), interno)
            val altura = 14f + 12f + (destaqueL?.height?.plus(4f) ?: 0f) + textoL.height + 12f
            garantir(altura)
            preenchimento.color = DESTAQUE
            val r = RectF(MARGEM, y, LARGURA - MARGEM, y + altura)
            canvas.drawRoundRect(r, 6f, 6f, preenchimento)
            preenchimento.color = PRIMARIA
            canvas.drawRect(MARGEM, y, MARGEM + 4f, y + altura, preenchimento)
            var yy = y + 14f
            canvas.drawText(rotulo, MARGEM + 14f, yy + 6f, paint(8f, negrito = true, cor = PRIMARIA))
            yy += 12f
            destaqueL?.let { desenharLayout(it, MARGEM + 14f, yy); yy += it.height + 4f }
            desenharLayout(textoL, MARGEM + 14f, yy)
            y += altura
        }

        fun alturaTexto(t: String, tamanho: Float): Float = layout(t, paint(tamanho), largura).height.toFloat()

        /** [reservar]: altura mínima do conteúdo que vem logo após o título, para não deixá-lo órfão. */
        fun secao(t: String, reservar: Float = 18f) {
            garantir(30f + reservar)
            y += 8f
            canvas.drawText(t.uppercase(), MARGEM, y + 10f, paint(10f, negrito = true, cor = PRIMARIA))
            preenchimento.color = PRIMARIA
            canvas.drawRect(MARGEM, y + 15f, MARGEM + 28f, y + 17f, preenchimento)
            y += 22f
        }

        fun espaco(px: Float) {
            y += px
        }

        fun texto(t: String, tamanho: Float = 10f, negrito: Boolean = false, cor: Int = TINTA) {
            val l = layout(t, paint(tamanho, negrito, cor), largura)
            garantir(l.height.toFloat())
            desenharLayout(l, MARGEM, y)
            y += l.height + 4f
        }

        fun indicadores(itens: List<Pair<String, String>>) {
            if (itens.isEmpty()) return
            val gap = 8f
            val w = (largura - gap * (itens.size - 1)) / itens.size
            val h = 36f
            garantir(h)
            itens.forEachIndexed { i, (nome, valor) ->
                val x = MARGEM + i * (w + gap)
                preenchimento.color = FUNDO_CARTAO
                canvas.drawRoundRect(RectF(x, y, x + w, y + h), 5f, 5f, preenchimento)
                canvas.drawText(nome, x + 10f, y + 14f, paint(8f, negrito = true, cor = CINZA))
                val vl = layout(valor, paint(10.5f, negrito = true, cor = TINTA), w - 20f)
                desenharLayout(vl, x + 10f, y + 19f)
            }
            y += h + 6f
        }

        fun tabela(colunas: List<Coluna>, linhas: List<List<Celula>>, fonte: Float = 9f, fundoLinha: (Int) -> Int? = { null }) {
            val somaPesos = colunas.sumOf { it.peso.toDouble() }.toFloat()
            val larguras = colunas.map { it.peso / somaPesos * largura }
            val pad = 4f

            fun cabecalho() {
                val layouts = colunas.mapIndexed { i, c -> layout(c.titulo, paint(fonte, negrito = true, cor = Color.WHITE), larguras[i] - 2 * pad) }
                val h = layouts.maxOf { it.height } + 2 * pad
                garantir(h + 20f)
                preenchimento.color = PRIMARIA
                canvas.drawRect(MARGEM, y, MARGEM + largura, y + h, preenchimento)
                var x = MARGEM
                layouts.forEachIndexed { i, l -> desenharLayout(l, x + pad, y + pad); x += larguras[i] }
                y += h
            }

            cabecalho()
            linhas.forEachIndexed { indice, linha ->
                // Célula com barra: número à esquerda (largura fixa) e barra ao lado, na mesma linha.
                val layouts = linha.mapIndexed { i, cel ->
                    val w = if (cel.barra != null) LARGURA_NUMERO_BARRA else larguras[i] - 2 * pad
                    layout(cel.texto, paint(fonte, cel.negrito, TINTA), w)
                }
                val h = layouts.maxOf { it.height } + 2 * pad
                if (y + h > limite) {
                    novaPagina()
                    cabecalho()
                }
                val fundo = fundoLinha(indice) ?: if (indice % 2 == 1) ZEBRA else null
                fundo?.let {
                    preenchimento.color = it
                    canvas.drawRect(MARGEM, y, MARGEM + largura, y + h, preenchimento)
                }
                var x = MARGEM
                linha.forEachIndexed { i, cel ->
                    cel.fundo?.let {
                        preenchimento.color = it
                        canvas.drawRect(x, y, x + larguras[i], y + h, preenchimento)
                    }
                    desenharLayout(layouts[i], x + pad, y + pad)
                    cel.barra?.let { f ->
                        val by = y + pad + layouts[i].height / 2f - 2.5f
                        val bx = x + pad + LARGURA_NUMERO_BARRA + 4f
                        val bw = larguras[i] - 2 * pad - LARGURA_NUMERO_BARRA - 4f
                        preenchimento.color = TRILHO
                        canvas.drawRoundRect(RectF(bx, by, bx + bw, by + 5f), 2.5f, 2.5f, preenchimento)
                        preenchimento.color = PRIMARIA
                        canvas.drawRoundRect(RectF(bx, by, bx + bw * f.coerceIn(0f, 1f), by + 5f), 2.5f, 2.5f, preenchimento)
                    }
                    x += larguras[i]
                }
                canvas.drawLine(MARGEM, y + h, MARGEM + largura, y + h, traco)
                y += h
            }
            y += 6f
        }

        fun finalizar(): Int {
            pagina?.let { rodape(it); pdf.finishPage(it) }
            pagina = null
            return numero
        }

        private fun desenharLayout(l: StaticLayout, x: Float, yTopo: Float) {
            val c = canvas
            c.save()
            c.translate(x, yTopo)
            l.draw(c)
            c.restore()
        }

        private fun paint(tamanho: Float, negrito: Boolean = false, cor: Int = TINTA) = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = tamanho
            color = cor
            typeface = if (negrito) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }

        private fun layout(t: String, tp: TextPaint, w: Float): StaticLayout =
            StaticLayout.Builder.obtain(t, 0, t.length, tp, w.toInt().coerceAtLeast(10))
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(1.5f, 1f)
                .setIncludePad(false)
                .build()
    }

    companion object {
        private const val LARGURA = 595f // A4 em pontos
        private const val ALTURA = 842f
        private const val MARGEM = 40f
        private const val LARGURA_NUMERO_BARRA = 44f
        private val PRIMARIA = Color.rgb(0x00, 0x6A, 0x62)
        private val PRIMARIA_ESCURA = Color.rgb(0x00, 0x4D, 0x47)
        private val CLARO = Color.rgb(0xD5, 0xEE, 0xEA)
        private val TINTA = Color.rgb(0x14, 0x2C, 0x2F)
        private val CINZA = Color.rgb(0x5A, 0x6E, 0x70)
        private val BORDA = Color.rgb(0xD6, 0xDE, 0xDE)
        private val ZEBRA = Color.rgb(0xF5, 0xF8, 0xF8)
        private val DESTAQUE = Color.rgb(0xE3, 0xF3, 0xF0)
        private val AJUSTADO = Color.rgb(0xFF, 0xF4, 0xDC)
        private val FUNDO_CARTAO = Color.rgb(0xF0, 0xF4, 0xF4)
        private val TRILHO = Color.rgb(0xE1, 0xE8, 0xE8)
    }
}
