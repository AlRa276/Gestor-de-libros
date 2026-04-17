package com.marianaalra.book.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class QuoteDomain(
    val id: Long,
    val bookId: Long,
    val textoCitado: String,
    val comentario: String?,
    val referenciaPagina: String?,
    val fechaCreacion: Long = System.currentTimeMillis()
){
    val fechaFormat: String
        get() {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("es-MX"))
            return sdf.format(Date(fechaCreacion))
        }
}