package com.marianaalra.booklog.data.repository.impl

import com.marianaalra.booklog.data.local.dao.UserDao
import com.marianaalra.booklog.data.local.entity.UserEntity
import com.marianaalra.booklog.data.mapper.toDomain
import com.marianaalra.booklog.domain.model.UserDomain
import com.marianaalra.booklog.domain.repository.AuthRepository

class AuthRepositoryImpl(private val dao: UserDao) : AuthRepository {

    // Variable temporal en memoria para saber quién está usando la app
    private var currentUser: UserDomain? = null

    override suspend fun login(correo: String, contrasena: String): Result<UserDomain> {
        val userEntity = dao.getUserByEmail(correo)
            ?: return Result.failure(Exception("El correo no está registrado"))

        // En una app real de producción, aquí se usaría un desencriptador. Por ahora lo validamos directo.
        if (userEntity.hashContrasena == contrasena) {
            currentUser = userEntity.toDomain()
            return Result.success(currentUser!!)
        }
        return Result.failure(Exception("Contraseña incorrecta"))
    }

    override suspend fun register(nombreUsuario: String, correo: String, contrasena: String): Result<UserDomain> {
        val existingUser = dao.getUserByEmail(correo)
        if (existingUser != null) {
            return Result.failure(Exception("El correo ya está en uso"))
        }

        val newUser = UserEntity(nombreUsuario = nombreUsuario, correo = correo, hashContrasena = contrasena)
        val insertedId = dao.insertUser(newUser)

        currentUser = UserDomain(id = insertedId, nombreUsuario = nombreUsuario, correo = correo)
        return Result.success(currentUser!!)
    }

    override suspend fun logout() {
        currentUser = null
    }

    override suspend fun getCurrentUser(): UserDomain? {
        return currentUser
    }
}