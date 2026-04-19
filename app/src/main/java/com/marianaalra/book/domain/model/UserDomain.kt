package com.marianaalra.book.domain.model

data class UserDomain(
    val id: Long = 0,
    val nombreUsuario: String,
    val correo: String
    // OJO: Nunca guardamos la contraseña (ni el hash) en el modelo de la sesión activa en el celular
    // por cuestiones de seguridad.
)