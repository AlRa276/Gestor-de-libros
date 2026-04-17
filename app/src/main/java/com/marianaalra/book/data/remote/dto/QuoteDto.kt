package com.marianaalra.book.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Cita desde la API remota.
 */
data class QuoteDto(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("lecturaId")
    val lecturaId: Long,
    @SerializedName("textoCitado")
    val textoCitado: String,
    @SerializedName("referenciaPagina")
    val referenciaPagina: String? = null,
    @SerializedName("comentario")
    val comentario: String? = null,
    @SerializedName("fechaCreacion")
    val fechaCreacion: Long? = null
)

