package com.marianaalra.book.data.repository.impl

import com.marianaalra.book.data.local.dao.SerieDao
import com.marianaalra.book.data.mapper.toDomain
import com.marianaalra.book.data.mapper.toEntity
import com.marianaalra.book.data.repository.remote.SerieRemoteRepository
import com.marianaalra.book.domain.model.SerieDomain
import com.marianaalra.book.domain.repository.SerieRepository
import com.marianaalra.book.domain.util.Resource
import kotlinx.coroutines.flow.map

class SerieRepositoryImpl(
    private val dao: SerieDao,
    private val remoteRepository: SerieRemoteRepository? = null
) : SerieRepository {
    override fun getSeriesForUser(userId: Long) =
        dao.getSeriesForUser(userId).map { it.map { e -> e.toDomain() } }

    override suspend fun searchSeries(userId: Long, query: String) =
        dao.searchSeries(userId, query).map { it.toDomain() }

    override suspend fun insertSerie(serie: SerieDomain): Long {
        if (remoteRepository != null) {
            when (val remote = remoteRepository.createSerie(serie)) {
                is Resource.Success -> {
                    val saved = remote.data
                    dao.insertSerie(saved.toEntity())
                    return saved.id
                }
                else -> Unit
            }
        }
        return dao.insertSerie(serie.toEntity())
    }

    override suspend fun deleteSerie(serie: SerieDomain) {
        remoteRepository?.deleteSerie(serie.id)
        dao.deleteSerie(serie.toEntity())
    }
}