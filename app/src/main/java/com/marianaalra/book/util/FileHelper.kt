package com.marianaalra.book.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor

fun copyPdfToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        // Creamos una carpeta "books" dentro del almacenamiento privado de la app
        val booksDir = File(context.filesDir, "books")
        if (!booksDir.exists()) booksDir.mkdirs()

        // Generamos un nombre único para no sobrescribir libros
        val fileName = "book_${System.currentTimeMillis()}.pdf"
        val destFile = File(booksDir, fileName)

        // Copiamos el contenido
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(destFile).use { output ->
                input.copyTo(output)
            }
        }

        // Devolvemos la ruta interna (ya es nuestra, sin permisos externos)
        destFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
fun extractPdfCover(context: Context, pdfPath: String): String? {
    return try {
        val coversDir = File(context.filesDir, "covers")
        if (!coversDir.exists()) coversDir.mkdirs()

        val coverFile = File(coversDir, "cover_${System.currentTimeMillis()}.png")

        val pdfFile = File(pdfPath)
        val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(fileDescriptor)

        val page = renderer.openPage(0) // Solo la primera página

        // Usamos un tamaño fijo para la portada (proporcional al ratio de un libro)
        val width = 300
        val height = (width / 0.72f).toInt()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        page.close()
        renderer.close()
        fileDescriptor.close()

        // Guardamos la imagen
        FileOutputStream(coverFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
        }
        bitmap.recycle()

        coverFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}