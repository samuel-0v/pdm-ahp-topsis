package br.edu.ufu.pdm.mcdm.presentation.exportacao

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import br.edu.ufu.pdm.mcdm.domain.exportacao.RelatorioGerado
import java.io.File

/**
 * Entrega o PDF gerado ao seletor de compartilhamento do sistema (DEC-032). O arquivo fica no
 * cache do app e é exposto apenas por URI temporária com permissão de leitura.
 * @return false se nenhum app puder receber o arquivo.
 */
fun compartilharRelatorio(context: Context, relatorio: RelatorioGerado): Boolean {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.relatorios", File(relatorio.caminhoArquivo))
    val envio = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, relatorio.nomeArquivo)
        clipData = ClipData.newRawUri(relatorio.nomeArquivo, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    return try {
        context.startActivity(Intent.createChooser(envio, "Compartilhar relatório PDF"))
        true
    } catch (_: ActivityNotFoundException) {
        false
    }
}
