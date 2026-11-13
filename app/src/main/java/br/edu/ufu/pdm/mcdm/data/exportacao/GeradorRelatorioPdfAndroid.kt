package br.edu.ufu.pdm.mcdm.data.exportacao

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
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
 */
class GeradorRelatorioPdfAndroid(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : GeradorRelatorioPdf {

    private val dataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("pt-BR")).withZone(ZoneId.systemDefault())

    override suspend fun gerar(conteudo: ConteudoRelatorio): RelatorioGerado = withContext(dispatcher) {
        val pdf = PdfDocument()
        val desenho = Desenho(pdf)
        try {
            desenho.titulo("Relatório de avaliação AHP-TOPSIS")
            desenho.texto(conteudo.titulo, tamanho = 15f, negrito = true)
            desenho.texto("Categoria: ${conteudo.categoria}")
            desenho.texto("PDF gerado em: ${dataHora.format(conteudo.geradoEm)}")
            conteudo.salvaEm?.let { desenho.texto("Versão salva em: ${dataHora.format(it)}") }
            desenho.texto("As datas acima não são datas de referência dos dados.", tamanho = 9f, cor = CINZA)
            desenho.espaco()

            desenho.secao("Situação")
            desenho.texto(conteudo.situacao)
            desenho.espaco()

            desenho.secao("Critérios e pesos AHP")
            conteudo.criterios.forEach { desenho.texto("• ${it.nome} (${it.unidade}) — ${it.natureza} — peso ${it.peso}") }
            desenho.espaco()

            desenho.secao("Indicadores AHP")
            desenho.texto(conteudo.indicadoresAhp.joinToString("   ") { "${it.first} ${it.second}" })
            desenho.espaco()

            desenho.secao("Resultado TOPSIS")
            conteudo.alternativas.forEach { a ->
                desenho.texto("${a.posicao} · ${a.nome} (${a.origem})", negrito = true)
                desenho.texto("C* ${a.coeficiente} · d+ ${a.dMais} · d− ${a.dMenos}", tamanho = 10f)
                a.valores.forEach { (criterio, valor) -> desenho.texto("   $criterio: $valor", tamanho = 10f) }
                desenho.espaco(6f)
            }

            desenho.secao("Método")
            desenho.texto(conteudo.metodo, tamanho = 9f, cor = CINZA)
            desenho.finalizar()

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

    private class Desenho(private val pdf: PdfDocument) {
        private var numero = 0
        private var pagina: PdfDocument.Page? = null
        private var y = 0f

        private fun canvas() = (pagina ?: novaPagina()).canvas

        private fun novaPagina(): PdfDocument.Page {
            pagina?.let { rodape(it); pdf.finishPage(it) }
            numero++
            val p = pdf.startPage(PdfDocument.PageInfo.Builder(LARGURA, ALTURA, numero).create())
            pagina = p
            y = MARGEM
            return p
        }

        private fun rodape(p: PdfDocument.Page) {
            val tp = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 8f; color = CINZA }
            p.canvas.drawText("PDM AHP-TOPSIS · página $numero", MARGEM, ALTURA - 24f, tp)
        }

        fun titulo(t: String) {
            canvas()
            texto(t, tamanho = 18f, negrito = true, cor = PRIMARIA)
        }

        fun secao(t: String) = texto(t.uppercase(), tamanho = 10f, negrito = true, cor = PRIMARIA)

        fun espaco(px: Float = 10f) {
            y += px
        }

        fun texto(t: String, tamanho: Float = 11f, negrito: Boolean = false, cor: Int = TINTA) {
            val tp = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = tamanho
                color = cor
                typeface = if (negrito) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            }
            val largura = (LARGURA - 2 * MARGEM).toInt()
            val layout = StaticLayout.Builder.obtain(t, 0, t.length, tp, largura)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL).setLineSpacing(2f, 1f).build()
            if (y + layout.height > ALTURA - MARGEM) novaPagina()
            val c = canvas()
            c.save()
            c.translate(MARGEM, y)
            layout.draw(c)
            c.restore()
            y += layout.height + 3f
        }

        fun finalizar() {
            pagina?.let { rodape(it); pdf.finishPage(it) }
            pagina = null
        }
    }

    companion object {
        private const val LARGURA = 595 // A4 em pontos
        private const val ALTURA = 842
        private const val MARGEM = 48f
        private val PRIMARIA = Color.rgb(0x00, 0x6A, 0x62)
        private val TINTA = Color.rgb(0x14, 0x2C, 0x2F)
        private val CINZA = Color.rgb(0x4B, 0x60, 0x62)
    }
}
