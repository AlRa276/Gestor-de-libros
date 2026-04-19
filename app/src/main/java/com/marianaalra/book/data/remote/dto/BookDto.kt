package com.marianaalra.book.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Libro (Lectura) desde la API remota.
 * Mapea los campos de la API con los campos de la app.
 */
data class BookDto(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("usuarioId")
    val usuarioId: Long,
    @SerializedName("rutaArchivo")
    val rutaArchivo: String? = null,
    @SerializedName("nombreArchivo")
    val nombreArchivo: String,
    @SerializedName("titulo")
    val titulo: String,
    @SerializedName("formato")
    val formato: String,
    @SerializedName("autor")
    val autor: String? = null,
    @SerializedName("serieId")
    val serieId: Long? = null,
    @SerializedName("progreso")
    val progreso: Float = 0f,
    @SerializedName("estado")
    val estado: String = "PENDIENTE",
    @SerializedName("coverPath")
    val coverPath: String? = null,
    @SerializedName("fechaCreacion")
    val fechaCreacion: Long? = null
)

