package com.marianaalra.book.data.repository.remote

import com.marianaalra.book.data.mapper.toDomain
import com.marianaalra.book.data.remote.api.ApiService
import com.marianaalra.book.data.remote.dto.NoteDto
import com.marianaalra.book.domain.model.NoteDomain
import com.marianaalra.book.domain.util.Resource

/**
 * Repositorio REMOTO para operaciones de Notas.
 */
class NoteRemoteRepository(private val apiService: ApiService) {

    suspend fun createNote(note: NoteDomain): Resource<NoteDomain> {
        return try {
            val noteDto = NoteDto(
                lecturaId = note.bookId,
                contenido = note.contenido,
                referenciaPagina = note.referenciaPagina
            )
            val response = apiService.createNote(noteDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al crear nota: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getNoteById(id: Long): Resource<NoteDomain> {
        return try {
            val response = apiService.getNoteById(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Nota no encontrada: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getNotesByLecturaId(lecturaId: Long): Resource<List<NoteDomain>> {
        return try {
            val response = apiService.getNotesByLecturaId(lecturaId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error al obtener notas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun updateNote(note: NoteDomain): Resource<NoteDomain> {
        return try {
            val noteDto = NoteDto(
                id = note.id,
                lecturaId = note.bookId,
                contenido = note.contenido,
                referenciaPagina = note.referenciaPagina
            )
            val response = apiService.updateNote(note.id, noteDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al actualizar nota: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun deleteNote(id: Long): Resource<Boolean> {
        return try {
            val response = apiService.deleteNote(id)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(Exception("Error al eliminar nota: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}

