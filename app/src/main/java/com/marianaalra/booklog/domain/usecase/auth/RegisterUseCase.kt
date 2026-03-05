package com.marianaalra.booklog.domain.usecase.auth

import com.marianaalra.booklog.domain.model.UserDomain
import com.marianaalra.booklog.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(nombreUsuario: String, correo: String, contrasena: String): Result<UserDomain> {
        if (nombreUsuario.isBlank() || correo.isBlank() || contrasena.isBlank()) {
            return Result.failure(Exception("Todos los campos son obligatorios"))
        }
        if (contrasena.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        return repository.register(nombreUsuario, correo, contrasena)
    }
}