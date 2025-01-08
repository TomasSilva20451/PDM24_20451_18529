package com.example.lojasocial.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.lojasocial.domain.model.Beneficiary
import java.io.File
import java.io.FileOutputStream

object Utils {

    // Exemplo existente
    fun exampleUtilFunction(): String {
        return "Hello from Utils!"
    }

    // Nova função para gerar PDF do Beneficiary
    fun generateBeneficiaryReport(context: Context, beneficiary: Beneficiary) {
        // 1) Cria documento PDF
        val pdfDocument = PdfDocument()

        // 2) Configura "tamanho da página" (A4 approx. a 72DPI)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

        // 3) Inicia página
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // Título
        paint.textSize = 16f
        paint.isFakeBoldText = true
        canvas.drawText("Relatório do Beneficiário", 50f, 50f, paint)

        // Dados (reduz um pouco o tamanho da fonte)
        paint.textSize = 14f
        paint.isFakeBoldText = false

        canvas.drawText("Nome: ${beneficiary.nome}", 50f, 100f, paint)
        canvas.drawText("Agregado Familiar: ${beneficiary.agregadoFamiliar}", 50f, 130f, paint)
        canvas.drawText("Data Nascimento: ${beneficiary.dataNascimento}", 50f, 160f, paint)
        canvas.drawText("Nacionalidade: ${beneficiary.nacionalidade}", 50f, 190f, paint)

        // Finaliza a página
        pdfDocument.finishPage(page)

        // 4) Define o caminho do arquivo
        val path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        // Ex.: /storage/emulated/0/Android/data/<package>/files/Documents
        val safeName = beneficiary.nome.replace(" ", "_")
        val file = File(path, "Relatorio_${safeName}.pdf")

        // 5) Salva o PDF
        FileOutputStream(file).use { output ->
            pdfDocument.writeTo(output)
        }
        pdfDocument.close()

        // Aqui você pode exibir um Toast ou Snackbar dizendo onde foi salvo
        // Ex.: Toast.makeText(context, "PDF gerado em: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    }
}