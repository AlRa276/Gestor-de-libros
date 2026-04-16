package com.marianaalra.booklog.data.repository.remote

import com.marianaalra.booklog.data.mapper.toDomain
import com.marianaalra.booklog.data.remote.api.ApiService
import com.marianaalra.booklog.data.remote.dto.ColeccionDto
import com.marianaalra.booklog.domain.model.ColeccionDomain
import com.marianaalra.booklog.domain.util.Resource

/**
 * Repositorio REMOTO para operaciones de Colecciones.
 */
class ColeccionRemoteRepository(private val apiService: ApiService) {

    suspend fun createColeccion(coleccion: ColeccionDomain): Resource<ColeccionDomain> {
        return try {
            val coleccionDto = ColeccionDto(
                usuarioId = coleccion.usuarioId,
                nombre = coleccion.nombre
            )
            val response = apiService.createColeccion(coleccionDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al crear colección: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getColeccionById(id: Long): Resource<ColeccionDomain> {
        return try {
            val response = apiService.getColeccionById(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Colección no encontrada: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getColeccionesByUsuarioId(usuarioId: Long): Resource<List<ColeccionDomain>> {
        return try {
            val response = apiService.getColeccionesByUsuarioId(usuarioId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error al obtener colecciones: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun updateColeccion(coleccion: ColeccionDomain): Resource<ColeccionDomain> {
        return try {
            val coleccionDto = ColeccionDto(
                id = coleccion.id,
                usuarioId = coleccion.usuarioId,
                nombre = coleccion.nombre
            )
            val response = apiService.updateColeccion(coleccion.id, coleccionDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al actualizar colección: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun deleteColeccion(id: Long): Resource<Boolean> {
        return try {
            val response = apiService.deleteColeccion(id)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(Exception("Error al eliminar colección: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}

