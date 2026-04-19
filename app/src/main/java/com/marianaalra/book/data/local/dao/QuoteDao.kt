package com.marianaalra.book.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marianaalra.book.data.local.entity.QuoteEntiny
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntiny)

    @Update
    suspend fun updateQuote(quote: QuoteEntiny)

    @Delete
    suspend fun deleteQuote(quote: QuoteEntiny)

    @Query("SELECT * FROM citas WHERE lecturaId = :bookId ORDER BY fechaCreacion DESC")
    fun getQuotesForBook(bookId: Long): Flow<List<QuoteEntiny>>
}