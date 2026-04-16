package com.marianaalra.booklog.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Usuario desde la API remota.
 */
data class UserDto(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("nombreUsuario")
    val nombreUsuario: String,
    @SerializedName("correo")
    val correo: String,
    @SerializedName("hashContrasena")
    val hashContrasena: String? = null,
    @SerializedName("fechaCreacion")
    val fechaCreacion: Long? = null
)

