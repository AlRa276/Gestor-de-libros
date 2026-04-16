package com.marianaalra.booklog.data.repository.remote

import com.marianaalra.booklog.data.mapper.toDomain
import com.marianaalra.booklog.data.remote.api.ApiService
import com.marianaalra.booklog.data.remote.dto.SerieDto
import com.marianaalra.booklog.domain.model.SerieDomain
import com.marianaalra.booklog.domain.util.Resource

/**
 * Repositorio REMOTO para operaciones de Series.
 */
class SerieRemoteRepository(private val apiService: ApiService) {

    suspend fun createSerie(serie: SerieDomain): Resource<SerieDomain> {
        return try {
            val serieDto = SerieDto(
                usuarioId = serie.usuarioId,
                nombre = serie.nombre
            )
            val response = apiService.createSerie(serieDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al crear serie: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getSerieById(id: Long): Resource<SerieDomain> {
        return try {
            val response = apiService.getSerieById(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Serie no encontrada: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getSeriesByUsuarioId(usuarioId: Long): Resource<List<SerieDomain>> {
        return try {
            val response = apiService.getSeriesByUsuarioId(usuarioId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toDomain() })
            } else {
                Resource.Error(Exception("Error al obtener series: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun updateSerie(serie: SerieDomain): Resource<SerieDomain> {
        return try {
            val serieDto = SerieDto(
                id = serie.id,
                usuarioId = serie.usuarioId,
                nombre = serie.nombre
            )
            val response = apiService.updateSerie(serie.id, serieDto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.toDomain())
            } else {
                Resource.Error(Exception("Error al actualizar serie: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun deleteSerie(id: Long): Resource<Boolean> {
        return try {
            val response = apiService.deleteSerie(id)
            if (response.isSuccessful) {
                Resource.Success(true)
            } else {
                Resource.Error(Exception("Error al eliminar serie: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}

