package com.marianaalra.booklog.data.local.dao

import androidx.room.*
import com.marianaalra.booklog.data.local.entity.SerieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SerieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerie(serie: SerieEntity): Long

    @Query("SELECT * FROM series WHERE usuarioId = :userId ORDER BY nombre ASC")
    fun getSeriesForUser(userId: Long): Flow<List<SerieEntity>>

    @Query("SELECT * FROM series WHERE usuarioId = :userId AND nombre LIKE '%' || :query || '%'")
    suspend fun searchSeries(userId: Long, query: String): List<SerieEntity>

    @Delete
    suspend fun deleteSerie(serie: SerieEntity)
}