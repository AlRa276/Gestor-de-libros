package com.marianaalra.book.data.repository.remote

import com.marianaalra.book.data.mapper.toDomain
import com.marianaalra.book.data.remote.api.ApiService
import com.marianaalra.book.data.remote.dto.BookDto
import com.marianaalra.book.domain.model.Book
import com.marianaalra.book.domain.util.Resource

/**
 * Repositorio REMOTO para operaciones de Libros.
 * Consumidor de la API REST BookLog.
 * 
 * Responsabilidades:
 * - Ejecutar operaciones CRUD contra la API remota
 * - Mapear DTOs → Domain models
 * - Manejar errores de red con Resource<T>
 */
class BookRemoteRepository(private val apiService: ApiService) {

    /**
     * Crear un libro en la API remota.
     */
    suspend fun createBook(book: Book): Resource<Book> {
        return try {
            val bookDto = BookDto(
                usuarioId = book.usuarioId,
                rutaArchivo = book.fileUri,
                nombreArchivo = book.nombreArchivo,
                titulo = book.title,
                formato = book.fileFormat,
                autor = book.author,
                serieId = book.serieId,
                progreso = book.progress,
                estado = book.status,
                coverPath = book.coverPath
            )
            
            val response = apiService.createBook(bookDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al crear libro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * Obtener un libro por ID.
     */
    suspend fun getBookById(id: Long): Resource<Book> {
        return try {
            val response = apiService.getBookById(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Libro no encontrado: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * Obtener todos los libros de un usuario.
     */
    suspend fun getBooksByUsuarioId(usuarioId: Long): Resource<List<Book>> {
        return try {
            val response = apiService.getBooksByUsuarioId(usuarioId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error al obtener libros: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * Obtener libros de un usuario filtrados por estado.
     */
    suspend fun getBooksByEstado(usuarioId: Long, estado: String): Resource<List<Book>> {
        return try {
            val response = apiService.getBooksByEstado(usuarioId, estado)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error al obtener libros por estado: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * Obtener libros de una serie.
     */
    suspend fun getBooksBySerieId(serieId: Long): Resource<List<Book>> {
        return try {
            val response = apiService.getBooksBySerieId(serieId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error al obtener libros de la serie: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * Buscar libros por título.
     */
    suspend fun searchByTitulo(usuarioId: Long, titulo: String): Resource<List<Book>> {
        return try {
            val response = apiService.searchByTitulo(usuarioId, titulo)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error en búsqueda por título: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * Buscar libros por autor.
     */
    suspend fun searchByAutor(usuarioId: Long, autor: String): Resource<List<Book>> {
        return try {
            val response = apiService.searchByAutor(usuarioId, autor)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error en búsqueda por autor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * Actualizar un libro.
     */
    suspend fun updateBook(book: Book): Resource<Book> {
        return try {
            val bookDto = BookDto(
                id = book.id,
                usuarioId = book.usuarioId,
                rutaArchivo = book.fileUri,
                nombreArchivo = book.nombreArchivo,
                titulo = book.title,
                formato = book.fileFormat,
                autor = book.author,
                serieId = book.serieId,
                progreso = book.progress,
                estado = book.status,
                coverPath = book.coverPath
            )
            
            val response = apiService.updateBook(book.id, bookDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al actualizar libro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * Actualizar progreso de lectura.
     */
    suspend fun updateBookProgress(bookId: Long, progreso: Float): Resource<Book> {
        return try {
            val response = apiService.updateBookProgress(bookId, progreso)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al actualizar progreso: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    /**
     * Eliminar un libro.
     */
    suspend fun deleteBook(bookId: Long): Resource<Boolean> {
        return try {
            val response = apiService.deleteBook(bookId)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(Exception("Error al eliminar libro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}

