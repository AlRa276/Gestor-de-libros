package com.marianaalra.book.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NoteDomain(
    val id: Long,
    val bookId: Long,
    val contenido: String,
    val referenciaPagina: String?,
    val fechaCreacion: Long = System.currentTimeMillis()
){
    // Calculado automáticamente, no se guarda en BD
    val fechaFormat: String
        get() {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("es-MX"))
            return sdf.format(Date(fechaCreacion))
        }
}