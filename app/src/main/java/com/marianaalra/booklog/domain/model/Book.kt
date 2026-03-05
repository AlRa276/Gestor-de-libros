package com.marianaalra.booklog.domain.model

data class Book(
    val id: Long = 0, // Necesario para la base de datos
    val usuarioId: Long,
    val title: String,
    val fileFormat: String,
    val progress: Float = 0f,
    val status: String = "PENDIENTE",
    val author: String? = null,
    val fileUri: String? = null, // 👈 NUEVO: Guardará la ruta del archivo
    val nombreArchivo: String? = null,
    val coverPath: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)