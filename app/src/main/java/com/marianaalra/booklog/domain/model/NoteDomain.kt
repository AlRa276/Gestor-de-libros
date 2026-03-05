package com.marianaalra.booklog.domain.model

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
            val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.forLanguageTag("es-MX"))
            return sdf.format(java.util.Date(fechaCreacion))
        }
}