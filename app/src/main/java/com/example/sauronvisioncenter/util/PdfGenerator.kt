package com.example.sauronvisioncenter.util

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.sauronvisioncenter.model.PrescriptionEntity
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import java.text.SimpleDateFormat
import java.util.*
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.font.PdfFontFactory
import java.io.OutputStream
import com.example.sauronvisioncenter.R
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Table
import java.io.ByteArrayOutputStream

class PdfGenerator(private val context: Context) {

    companion object {
        private const val TAG = "PdfGenerator"
    }

    fun generatePrescriptionPdf(prescription: PrescriptionEntity): Uri? {
        return try {
            val fileName = "Receita_${prescription.patientName}_${
                SimpleDateFormat(
                    "yyyyMMdd_HHmmss", Locale.getDefault()
                ).format(Date())
            }.pdf"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(
                MediaStore.Files.getContentUri(
                    "external"
                ), contentValues
            )

            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    createPdfContent(outputStream, prescription)
                }
                Log.d(TAG, "PDF salvo com sucesso: $uri")
                uri
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao gerar PDF", e)
            null
        }
    }

    private fun createPdfContent(outputStream: OutputStream, prescription: PrescriptionEntity) {
        PdfDocument(PdfWriter(outputStream)).use { pdfDoc ->
            Document(pdfDoc).use { doc ->
                val pageSize = PageSize.A4
                pdfDoc.addNewPage(pageSize)

                Document(pdfDoc).use { doc ->
                    doc.setMargins(50f, 30f, 50f, 30f)

                    val bitmap =
                        BitmapFactory.decodeResource(context.resources, R.drawable.not_sauron)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    val pdfImage = Image(ImageDataFactory.create(byteArray))

                    val headerTable = Table(floatArrayOf(1f, 2f))

                    pdfImage.setWidth(60f)
                    pdfImage.setAutoScale(true)

                    headerTable.addCell(Cell().add(pdfImage).setBorder(null))

                    // Cabeçalho
                    val currentDate = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm", Locale.getDefault()
                    ).format(Date())
                    val headerText = Paragraph()
                        .add("SAURON VISION CENTER\n")
                        .setBold()
                        .setFontSize(14f)
                        .add("RECEITA ÓPTICA\n")
                        .setItalic()
                        .setFontSize(12f)
                        .add("Data de emissão: $currentDate")
                        .setFontSize(10f)

                    val textCell = Cell().add(headerText)
                        .setBorder(null)
                        .setTextAlignment(TextAlignment.CENTER)
                    headerTable.addCell(textCell)

                    doc.add(headerTable)
                    doc.add(Paragraph("\n"))

                    addFieldIfNotEmpty(doc, "Nome", prescription.patientName)
                    addFieldIfNotEmpty(doc, "Data", prescription.lastCheckup)

                    doc.add(
                        Paragraph("\nPRESCRIÇÃO ÓPTICA")
                            .setBold()
                            .setItalic()
                            .setFontSize(12f)
                    )

                    doc.add(
                        Paragraph("OLHO   ESF       CIL      EIXO       AV")
                            .setBold()
                            .setFontSize(10f)
                            .setFont(PdfFontFactory.createFont(StandardFonts.COURIER))
                    )

                    val odSphere =
                        if (prescription.rightEyeSphere.isBlank()) "-" else prescription.rightEyeSphere
                    val odCylinder =
                        if (prescription.rightEyeCylinder.isBlank()) "-" else prescription.rightEyeCylinder
                    val odAxis =
                        if (prescription.rightEyeAxis.isBlank()) "-" else prescription.rightEyeAxis
                    val odAV =
                        if (prescription.rightEyeAV.isBlank()) "-" else prescription.rightEyeAV

                    val odLine = String.format(
                        "OD   %-10s %-10s %-10s %-10s",
                        odSphere,
                        odCylinder,
                        odAxis,
                        odAV
                    )
                    doc.add(
                        Paragraph(odLine)
                            .setFontSize(12f)
                            .setFont(PdfFontFactory.createFont(StandardFonts.COURIER))
                    )

                    val oeSphere =
                        if (prescription.leftEyeSphere.isBlank()) "-" else prescription.leftEyeSphere
                    val oeCylinder =
                        if (prescription.leftEyeCylinder.isBlank()) "-" else prescription.leftEyeCylinder
                    val oeAxis =
                        if (prescription.leftEyeAxis.isBlank()) "-" else prescription.leftEyeAxis
                    val oeAV = if (prescription.leftEyeAV.isBlank()) "-" else prescription.leftEyeAV

                    val oeLine = String.format(
                        "OE   %-10s %-10s %-10s %-10s",
                        oeSphere,
                        oeCylinder,
                        oeAxis,
                        oeAV
                    )
                    doc.add(
                        Paragraph(oeLine)
                            .setFontSize(12f)
                            .setFont(PdfFontFactory.createFont(StandardFonts.COURIER))
                    )

                    doc.add(Paragraph("\n"))

                    addFieldIfNotEmpty(doc, "ADD", prescription.addition)
                    addFieldIfNotEmpty(doc, "Tipo de lente", prescription.lensType)
                    addFieldIfNotEmpty(doc, "Cor da lente", prescription.lensColor)

                    doc.add(
                        Paragraph("\nOBSERVAÇÕES")
                            .setBold()
                            .setItalic()
                            .setFontSize(14f)
                    )

                    addFieldIfNotEmpty(doc, "Observações", prescription.observations)

                    // Rodapé
                    doc.add(
                        Paragraph("\n\n_________________________________________")
                            .setFontSize(10f)
                            .setTextAlignment(TextAlignment.CENTER)
                    )

                    doc.add(
                        Paragraph("O Olho que Tudo Vê - RNO111A\n\nBacharel em Optometria")
                            .setFontSize(10f)
                            .setTextAlignment(TextAlignment.CENTER)
                    )
                }
            }
        }
    }

    private fun addFieldIfNotEmpty(doc: Document, label: String, value: String?) {
        if (!value.isNullOrBlank()) {
            if (label == "Observações") {
                doc.add(
                    Paragraph(label + ":")
                        .setItalic()
                        .setFontSize(12f)
                )

                val observationsParagraph = Paragraph(value)
                    .setFontSize(12f)
                    .setMarginLeft(20f)
                    .setFixedLeading(14f)

                doc.add(observationsParagraph) // Corrigido o typo (observations)
            } else {
                doc.add(
                    Paragraph("$label: $value")
                        .setItalic()
                        .setFontSize(12f)
                )
            }
        }
    }
}