package com.marianaalra.booklog.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Colección desde la API remota.
 */
data class ColeccionDto(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("usuarioId")
    val usuarioId: Long,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("fechaCreacion")
    val fechaCreacion: Long? = null
)

