package com.marianaalra.booklog.data.repository.impl

import com.marianaalra.booklog.data.local.dao.QuoteDao
import com.marianaalra.booklog.data.mapper.toDomain
import com.marianaalra.booklog.data.mapper.toEntity
import com.marianaalra.booklog.domain.model.QuoteDomain
import com.marianaalra.booklog.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuoteRepositoryImpl(private val dao: QuoteDao) : QuoteRepository {

    override fun getQuotesForBook(bookId: Long): Flow<List<QuoteDomain>> {
        return dao.getQuotesForBook(bookId).map { lista -> lista.map { it.toDomain() } }
    }

    override suspend fun insertQuote(quote: QuoteDomain) {
        dao.insertQuote(quote.toEntity())
    }

    override suspend fun updateQuote(quote: QuoteDomain) {
        dao.updateQuote(quote.toEntity())
    }

    override suspend fun deleteQuote(quote: QuoteDomain) {
        dao.deleteQuote(quote.toEntity())
    }
}