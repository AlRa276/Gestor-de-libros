package com.marianaalra.booklog.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la relación Lectura-Colección desde la API remota.
 */
data class LecturaColeccionDto(
    @SerializedName("lecturaId")
    val lecturaId: Long,
    @SerializedName("coleccionId")
    val coleccionId: Long
)

