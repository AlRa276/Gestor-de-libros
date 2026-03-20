package com.marianaalra.booklog.data.repository.impl

import com.marianaalra.booklog.data.local.dao.SerieDao
import com.marianaalra.booklog.data.mapper.toDomain
import com.marianaalra.booklog.data.mapper.toEntity
import com.marianaalra.booklog.domain.model.SerieDomain
import com.marianaalra.booklog.domain.repository.SerieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SerieRepositoryImpl(private val dao: SerieDao) : SerieRepository {
    override fun getSeriesForUser(userId: Long) =
        dao.getSeriesForUser(userId).map { it.map { e -> e.toDomain() } }

    override suspend fun searchSeries(userId: Long, query: String) =
        dao.searchSeries(userId, query).map { it.toDomain() }

    override suspend fun insertSerie(serie: SerieDomain): Long =
        dao.insertSerie(serie.toEntity())

    override suspend fun deleteSerie(serie: SerieDomain) =
        dao.deleteSerie(serie.toEntity())
}