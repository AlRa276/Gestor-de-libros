package com.marianaalra.book.domain.repository

import com.marianaalra.book.domain.model.QuoteDomain
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    // Trae todas las citas copiadas de un libro en específico
    fun getQuotesForBook(bookId: Long): Flow<List<QuoteDomain>>

    suspend fun insertQuote(quote: QuoteDomain)

    suspend fun updateQuote(quote: QuoteDomain)

    suspend fun deleteQuote(quote: QuoteDomain)
}