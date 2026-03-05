package com.marianaalra.booklog.domain.usecase.auth

import com.marianaalra.booklog.domain.model.UserDomain
import com.marianaalra.booklog.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(correo: String, contrasena: String): Result<UserDomain> {
        if (correo.isBlank() || contrasena.isBlank()) {
            return Result.failure(Exception("El correo y la contraseña son obligatorios"))
        }
        return repository.login(correo, contrasena)
    }
}