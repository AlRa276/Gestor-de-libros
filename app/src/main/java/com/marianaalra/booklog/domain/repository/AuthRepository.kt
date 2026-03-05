package com.marianaalra.booklog.domain.repository


import com.marianaalra.booklog.domain.model.UserDomain

interface AuthRepository {
    // Result<> nos ayuda a saber si el login/registro fue exitoso o falló
    suspend fun login(correo: String, contrasena: String): Result<UserDomain>

    suspend fun register(nombreUsuario: String, correo: String, contrasena: String): Result<UserDomain>

    suspend fun logout()

    // Para saber si el usuario ya había iniciado sesión antes de abrir la app
    suspend fun getCurrentUser(): UserDomain?
}