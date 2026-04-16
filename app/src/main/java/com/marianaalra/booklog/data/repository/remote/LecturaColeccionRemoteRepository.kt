package com.marianaalra.booklog.data.repository.remote

import com.marianaalra.booklog.data.mapper.toDomain
import com.marianaalra.booklog.data.remote.api.ApiService
import com.marianaalra.booklog.data.remote.dto.LecturaColeccionDto
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.model.ColeccionDomain
import com.marianaalra.booklog.domain.util.Resource

/**
 * Repositorio REMOTO para operaciones de Lectura-Colección (relación Many-to-Many).
 */
class LecturaColeccionRemoteRepository(private val apiService: ApiService) {

    suspend fun addBookToColeccion(lecturaId: Long, coleccionId: Long): Resource<Boolean> {
        return try {
            val lecturaColeccionDto = LecturaColeccionDto(
                lecturaId = lecturaId,
                coleccionId = coleccionId
            )
            val response = apiService.addBookToColeccion(lecturaColeccionDto)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(Exception("Error al agregar libro a colección: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getBooksByColeccion(coleccionId: Long): Resource<List<Book>> {
        return try {
            val response = apiService.getBooksByColeccion(coleccionId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error al obtener libros de la colección: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getColeccionesByBook(lecturaId: Long): Resource<List<ColeccionDomain>> {
        return try {
            val response = apiService.getColeccionesByBook(lecturaId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error al obtener colecciones del libro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun removeBookFromColeccion(lecturaId: Long, coleccionId: Long): Resource<Boolean> {
        return try {
            val response = apiService.removeBookFromColeccion(lecturaId, coleccionId)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(Exception("Error al remover libro de colección: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}

