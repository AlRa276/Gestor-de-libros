package com.marianaalra.booklog.domain.model

data class NoteDomain(
    val id: Int,
    val bookId: Long,
    val contenido: String,
    val referenciaPagina: String?,
    val fechaCreacion: Long = System.currentTimeMillis()
)