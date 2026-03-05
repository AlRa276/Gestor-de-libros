package com.marianaalra.booklog.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

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