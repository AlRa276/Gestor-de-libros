package com.marianaalra.book.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.marianaalra.book.R
import com.marianaalra.book.ui.viewmodel.MonthStats
import java.io.File

object PdfGenerator {

    fun exportStatisticsToPdf(
        context: Context,
        totalFinalizadas: Int,
        totalEnProgreso: Int,
        monthStats: List<MonthStats>
    ) {
        val pdfDocument = PdfDocument()

        // 1. Cargar tus fuentes personalizadas (Poppins)
        val poppinsBold = ResourcesCompat.getFont(context, R.font.poppins_bold)
        val poppinsRegular = ResourcesCompat.getFont(context, R.font.poppins_regular)

        // Configuración de la página
        val pageWidth = 595
        val pageHeight = 842
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        // 2. Definir pinceles (Paints) con colores y fuentes
        val primaryColor = Color.parseColor("#4A6572") // Un azul grisáceo elegante (cámbialo por tu color primario)
        val backgroundColor = Color.parseColor("#F5F5F5") // Gris muy clarito para fondos
        val textColor = Color.parseColor("#333333") // Gris oscuro para texto

        val headerBackgroundPaint = Paint().apply { color = primaryColor }
        val cardBackgroundPaint = Paint().apply { color = backgroundColor }

        val titleTextPaint = Paint().apply {
            typeface = poppinsBold
            textSize = 24f
            color = Color.WHITE
        }

        val subtitleTextPaint = Paint().apply {
            typeface = poppinsBold
            textSize = 16f
            color = primaryColor
        }

        val normalTextPaint = Paint().apply {
            typeface = poppinsRegular
            textSize = 14f
            color = textColor
        }

        var yPos = 0f

        // --- DIBUJAR ENCABEZADO ---
        // Dibujamos un rectángulo de color en la parte superior
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), 100f, headerBackgroundPaint)
        canvas.drawText("BookLog", 40f, 50f, titleTextPaint)
        titleTextPaint.textSize = 14f
        canvas.drawText("Reporte de Estadísticas", 40f, 75f, titleTextPaint)

        yPos = 140f

        // --- DIBUJAR RESUMEN GENERAL (Estilo Tarjeta) ---
        canvas.drawText("Resumen General", 40f, yPos, subtitleTextPaint)
        yPos += 20f

        // Rectángulo con bordes redondeados para el resumen
        val cardRect = RectF(40f, yPos, pageWidth - 40f, yPos + 80f)
        canvas.drawRoundRect(cardRect, 12f, 12f, cardBackgroundPaint)

        yPos += 35f
        canvas.drawText("Finalizadas: $totalFinalizadas", 60f, yPos, normalTextPaint.apply { typeface = poppinsBold })
        canvas.drawText("En Progreso: $totalEnProgreso", 250f, yPos, normalTextPaint)

        yPos += 80f

        // --- DIBUJAR DETALLE POR MES ---
        canvas.drawText("Lecturas Recientes", 40f, yPos, subtitleTextPaint)
        yPos += 20f

        // Dibujar una línea separadora debajo del subtítulo
        val linePaint = Paint().apply { color = Color.LTGRAY; strokeWidth = 1f }
        canvas.drawLine(40f, yPos, pageWidth - 40f, yPos, linePaint)
        yPos += 30f

        monthStats.forEach { stat ->
            // Etiqueta del mes en negrita
            normalTextPaint.typeface = poppinsBold
            canvas.drawText(stat.label, 40f, yPos, normalTextPaint)

            // Datos del mes en fuente regular
            normalTextPaint.typeface = poppinsRegular
            canvas.drawText("Finalizadas: ${stat.finalizadas}", 180f, yPos, normalTextPaint)
            canvas.drawText("Progreso: ${stat.enProgreso}", 320f, yPos, normalTextPaint)
            canvas.drawText("Pendientes: ${stat.pendientes}", 450f, yPos, normalTextPaint)

            yPos += 15f
            // Pequeña línea divisoria entre meses
            canvas.drawLine(40f, yPos, pageWidth - 40f, yPos, Paint().apply { color = Color.parseColor("#EEEEEE") })
            yPos += 25f
        }

        // --- PIE DE PÁGINA ---
        val footerPaint = Paint().apply {
            typeface = poppinsRegular
            textSize = 10f
            color = Color.GRAY
            textAlign = Paint.Align.CENTER // Centrar texto
        }
        canvas.drawText("Generado automáticamente por BookLog App", pageWidth / 2f, pageHeight - 40f, footerPaint)

        // Finalizar y guardar (Se mantiene tu lógica de MediaStore intacta)
        pdfDocument.finishPage(page)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "Estadisticas_BookLog.pdf")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                    Toast.makeText(context, "PDF estilizado guardado en Descargas", Toast.LENGTH_LONG).show()
                }
            } else {
                val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                val file = File(directory, "Estadisticas_BookLog.pdf")
                java.io.FileOutputStream(file).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                Toast.makeText(context, "PDF estilizado guardado", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al guardar el PDF", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }
}