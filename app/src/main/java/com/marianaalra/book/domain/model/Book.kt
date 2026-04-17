package com.marianaalra.book.domain.model

data class Book(
    val id: Long = 0, // Necesario para la base de datos
    val usuarioId: Long = 0,
    val title: String,
    val fileFormat: String,
    val progress: Float = 0f,
    val status: String = "PENDIENTE",
    val author: String? = null,
    val serieId: Long? = null,
    val fileUri: String, // 👈 NUEVO: Guardará la ruta del archivo
    val nombreArchivo: String,
    val coverPath: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)