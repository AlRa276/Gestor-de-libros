package com.marianaalra.booklog.domain.model

data class NoteDomain(
    val id: Int,
    val contenido: String,
    val referenciaPagina: String?,
    val fechaFormat: String
)