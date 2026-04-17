package com.marianaalra.book.data.repository.remote

import com.marianaalra.book.data.mapper.toDomain
import com.marianaalra.book.data.remote.api.ApiService
import com.marianaalra.book.data.remote.dto.QuoteDto
import com.marianaalra.book.domain.model.QuoteDomain
import com.marianaalra.book.domain.util.Resource

/**
 * Repositorio REMOTO para operaciones de Citas.
 */
class QuoteRemoteRepository(private val apiService: ApiService) {

    suspend fun createQuote(quote: QuoteDomain): Resource<QuoteDomain> {
        return try {
            val quoteDto = QuoteDto(
                lecturaId = quote.bookId,
                textoCitado = quote.textoCitado,
                referenciaPagina = quote.referenciaPagina,
                comentario = quote.comentario
            )
            val response = apiService.createQuote(quoteDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al crear cita: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getQuoteById(id: Long): Resource<QuoteDomain> {
        return try {
            val response = apiService.getQuoteById(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Cita no encontrada: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getQuotesByLecturaId(lecturaId: Long): Resource<List<QuoteDomain>> {
        return try {
            val response = apiService.getQuotesByLecturaId(lecturaId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error al obtener citas: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun updateQuote(quote: QuoteDomain): Resource<QuoteDomain> {
        return try {
            val quoteDto = QuoteDto(
                id = quote.id,
                lecturaId = quote.bookId,
                textoCitado = quote.textoCitado,
                referenciaPagina = quote.referenciaPagina,
                comentario = quote.comentario
            )
            val response = apiService.updateQuote(quote.id, quoteDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al actualizar cita: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun deleteQuote(id: Long): Resource<Boolean> {
        return try {
            val response = apiService.deleteQuote(id)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(Exception("Error al eliminar cita: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}

