package com.marianaalra.book.data.repository.impl

import com.marianaalra.book.data.local.dao.UserDao
import com.marianaalra.book.data.local.entity.UserEntity
import com.marianaalra.book.data.mapper.toDomain
import com.marianaalra.book.data.mapper.toEntity
import com.marianaalra.book.data.remote.api.ApiService
import com.marianaalra.book.data.remote.dto.UserDto
import com.marianaalra.book.domain.model.UserDomain
import com.marianaalra.book.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val dao: UserDao,
    private val apiService: ApiService? = null
) : AuthRepository {

    // Variable temporal en memoria para saber quién está usando la app
    private var currentUser: UserDomain? = null

    override suspend fun login(correo: String, contrasena: String): Result<UserDomain> {
        if (apiService != null) {
            try {
                val remoteResponse = apiService.getUserByEmail(correo)
                val remoteUser = remoteResponse.body()
                if (remoteResponse.isSuccessful && remoteUser != null) {
                    if (remoteUser.hashContrasena == contrasena) {
                        dao.insertUser(remoteUser.toEntity())
                        currentUser = UserDomain(
                            id = remoteUser.id,
                            nombreUsuario = remoteUser.nombreUsuario,
                            correo = remoteUser.correo
                        )
                        return Result.success(currentUser!!)
                    }
                    return Result.failure(Exception("Credenciales inválidas"))
                }
            } catch (_: Exception) {
                // Si la API falla por red/timeout, usamos fallback local.
            }
        }

        val userEntity = dao.getUserByEmail(correo)
            ?: return Result.failure(Exception("Credenciales inválidas"))

        // En una app real de producción, aquí se usaría un desencriptador. Por ahora lo validamos directo.
        if (userEntity.hashContrasena == contrasena) {
            currentUser = userEntity.toDomain()
            return Result.success(currentUser!!)
        }
        return Result.failure(Exception("Credenciales inválidas"))
    }

    override suspend fun register(nombreUsuario: String, correo: String, contrasena: String): Result<UserDomain> {
        if (apiService != null) {
            try {
                val emailExists = apiService.existsEmail(correo)
                if (emailExists.isSuccessful && emailExists.body() == true) {
                    return Result.failure(Exception("El correo ya está en uso"))
                }

                val usernameExists = apiService.existsUsername(nombreUsuario)
                if (usernameExists.isSuccessful && usernameExists.body() == true) {
                    return Result.failure(Exception("El nombre de usuario ya está en uso"))
                }

                val remoteCreate = apiService.createUser(
                    UserDto(
                        nombreUsuario = nombreUsuario,
                        correo = correo,
                        hashContrasena = contrasena
                    )
                )

                val createdUser = remoteCreate.body()
                if (remoteCreate.isSuccessful && createdUser != null) {
                    dao.insertUser(createdUser.toEntity())
                    currentUser = UserDomain(
                        id = createdUser.id,
                        nombreUsuario = createdUser.nombreUsuario,
                        correo = createdUser.correo
                    )
                    return Result.success(currentUser!!)
                }
            } catch (_: Exception) {
                // Si la API falla, usamos fallback local para no bloquear el registro offline.
            }
        }

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