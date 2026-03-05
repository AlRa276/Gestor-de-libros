package com.marianaalra.booklog.domain.model

data class QuoteDomain(
    val id: Int,
    val bookId: Long,
    val textoCitado: String,
    val comentario: String?,
    val referenciaPagina: String?,
    val fechaCreacion: Long = System.currentTimeMillis()
)