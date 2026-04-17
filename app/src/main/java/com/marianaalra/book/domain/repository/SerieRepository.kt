package com.marianaalra.book.domain.repository

import com.marianaalra.book.domain.model.SerieDomain
import kotlinx.coroutines.flow.Flow

interface SerieRepository {
    fun getSeriesForUser(userId: Long): Flow<List<SerieDomain>>
    suspend fun searchSeries(userId: Long, query: String): List<SerieDomain>
    suspend fun insertSerie(serie: SerieDomain): Long
    suspend fun deleteSerie(serie: SerieDomain)
}