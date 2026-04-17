package com.marianaalra.book.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Nota desde la API remota.
 */
data class NoteDto(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("lecturaId")
    val lecturaId: Long,
    @SerializedName("contenido")
    val contenido: String,
    @SerializedName("referenciaPagina")
    val referenciaPagina: String? = null,
    @SerializedName("fechaCreacion")
    val fechaCreacion: Long? = null
)

