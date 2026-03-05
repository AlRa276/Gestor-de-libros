package com.marianaalra.booklog.domain.model

data class QuoteDomain(
    val id: Int,
    val textoCitado: String,
    val comentario: String?,
    val referenciaPagina: String?,
    val fechaFormat: String
)