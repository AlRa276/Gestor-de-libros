package com.marianaalra.book.data.repository.impl

import com.marianaalra.book.data.local.dao.UserDao
import com.marianaalra.book.data.mapper.toDomain
import com.marianaalra.book.data.mapper.toEntity
import com.marianaalra.book.data.remote.api.ApiService
import com.marianaalra.book.data.remote.dto.UserDto
import com.marianaalra.book.domain.model.UserDomain
import com.marianaalra.book.domain.util.Resource

/**
 * Implementación REMOTA del AuthRepository.
 * 
 * Realiza login y registro contra la API remota (Railway.app),
 * y cachea el usuario en la BD local (Room) para acceso offline.
 * 
 * Estrategia:
 * 1. Intenta autenticar contra la API remota
 * 2. Si éxito, guarda el usuario en BD local
 * 3. Si fallo de red, intenta usar caché local
 */
class AuthRemoteRepositoryImpl(
    private val apiService: ApiService,
    private val userDao: UserDao
) {

    // Usuario actualmente logueado en esta sesión
    private var currentUser: UserDomain? = null

    /**
     * LOGIN REMOTO
     * Autentica el usuario contra la API remota.
     * 
     * @param correo Email del usuario
     * @param contrasena Contraseña en texto plano
     * @return Resource<UserDomain> con el resultado de la operación
     */
    suspend fun loginRemote(correo: String, contrasena: String): Resource<UserDomain> {
        return try {
            // Buscar usuario por correo en la API
            val response = apiService.getUserByEmail(correo)
            
            if (response.isSuccessful && response.body() != null) {
                val userDto = response.body()!!
                
                // Validar contraseña
                if (userDto.hashContrasena == contrasena) {
                    // Guardar en caché local
                    val userEntity = userDto.toEntity()
                    userDao.insertUser(userEntity)
                    
                    // Establecer usuario activo
                    currentUser = userDto.toDomain()
                    
                    Resource.Success(currentUser!!)
                } else {
                    Resource.Error(Exception("Credenciales inválidas"))
                }
            } else {
                Resource.Error(Exception("Usuario no encontrado (${response.code()})"))
            }
        } catch (e: Exception) {
            // Si hay error de red, intentar con caché local
            loginLocal(correo, contrasena)
        }
    }

    /**
     * LOGIN LOCAL (fallback)
     * Autentica contra la BD local (Room) cuando no hay conexión a internet.
     * 
     * @param correo Email del usuario
     * @param contrasena Contraseña en texto plano
     * @return Resource<UserDomain> con el resultado de la operación
     */
    private suspend fun loginLocal(correo: String, contrasena: String): Resource<UserDomain> {
        return try {
            val userEntity = userDao.getUserByEmail(correo)
                ?: return Resource.Error(Exception("Usuario no encontrado en caché local"))
            
            if (userEntity.hashContrasena == contrasena) {
                currentUser = userEntity.toDomain()
                Resource.Success(currentUser!!)
            } else {
                Resource.Error(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * REGISTRO REMOTO
     * Crea un nuevo usuario en la API remota.
     * 
     * @param nombreUsuario Nombre de usuario único
     * @param correo Email del usuario
     * @param contrasena Contraseña en texto plano
     * @return Resource<UserDomain> con el resultado de la operación
     */
    suspend fun registerRemote(
        nombreUsuario: String,
        correo: String,
        contrasena: String
    ): Resource<UserDomain> {
        return try {
            // Verificar que el usuario no exista en la API
            val existsEmailResponse = apiService.existsEmail(correo)
            if (existsEmailResponse.isSuccessful && existsEmailResponse.body() == true) {
                return Resource.Error(Exception("El correo ya está registrado"))
            }
            
            val existsUsernameResponse = apiService.existsUsername(nombreUsuario)
            if (existsUsernameResponse.isSuccessful && existsUsernameResponse.body() == true) {
                return Resource.Error(Exception("El nombre de usuario ya está en uso"))
            }
            
            // Crear el usuario
            val newUserDto = UserDto(
                nombreUsuario = nombreUsuario,
                correo = correo,
                hashContrasena = contrasena
            )
            
            val createResponse = apiService.createUser(newUserDto)
            if (createResponse.isSuccessful && createResponse.body() != null) {
                val createdUserDto = createResponse.body()!!
                
                // Guardar en caché local
                val userEntity = createdUserDto.toEntity()
                userDao.insertUser(userEntity)
                
                // Establecer usuario activo
                currentUser = createdUserDto.toDomain()
                
                Resource.Success(currentUser!!)
            } else {
                Resource.Error(Exception("Error al crear usuario (${createResponse.code()})"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * LOGOUT
     * Cierra la sesión del usuario activo.
     */
    suspend fun logout() {
        currentUser = null
    }

    /**
     * Obtener usuario actualmente logueado.
     */
    suspend fun getCurrentUser(): UserDomain? {
        return currentUser
    }
}

