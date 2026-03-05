package com.marianaalra.booklog.domain.model

data class Book(
    val title: String,
    val fileFormat: String,
    val progress: Float,
    val status: String,
    val fileUri: String? = null, // 👈 NUEVO: Guardará la ruta del archivo
    val nombreArchivo: String? = null,
    val coverPath: String? = null
)