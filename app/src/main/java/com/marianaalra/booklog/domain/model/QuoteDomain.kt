package com.marianaalra.booklog.domain.model

data class QuoteDomain(
    val id: Int,
    val bookId: Long,
    val textoCitado: String,
    val comentario: String?,
    val referenciaPagina: String?,
    val fechaCreacion: Long = System.currentTimeMillis()
){
    val fechaFormat: String
        get() {
            val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.forLanguageTag("es-MX"))
            return sdf.format(java.util.Date(fechaCreacion))
        }
}