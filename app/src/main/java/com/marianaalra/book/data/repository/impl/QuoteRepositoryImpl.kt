package com.marianaalra.book.data.repository.impl

import com.marianaalra.book.data.local.dao.QuoteDao
import com.marianaalra.book.data.mapper.toDomain
import com.marianaalra.book.data.mapper.toEntity
import com.marianaalra.book.data.repository.remote.QuoteRemoteRepository
import com.marianaalra.book.domain.model.QuoteDomain
import com.marianaalra.book.domain.repository.QuoteRepository
import com.marianaalra.book.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuoteRepositoryImpl(
    private val dao: QuoteDao,
    private val remoteRepository: QuoteRemoteRepository? = null
) : QuoteRepository {

    override fun getQuotesForBook(bookId: Long): Flow<List<QuoteDomain>> {
        return dao.getQuotesForBook(bookId).map { lista -> lista.map { it.toDomain() } }
    }

    override suspend fun insertQuote(quote: QuoteDomain) {
        if (remoteRepository != null) {
            when (val remote = remoteRepository.createQuote(quote)) {
                is Resource.Success -> {
                    dao.insertQuote(remote.data.toEntity())
                    return
                }
                else -> Unit
            }
        }
        dao.insertQuote(quote.toEntity())
    }

    override suspend fun updateQuote(quote: QuoteDomain) {
        if (remoteRepository != null) {
            when (val remote = remoteRepository.updateQuote(quote)) {
                is Resource.Success -> {
                    dao.updateQuote(remote.data.toEntity())
                    return
                }
                else -> Unit
            }
        }
        dao.updateQuote(quote.toEntity())
    }

    override suspend fun deleteQuote(quote: QuoteDomain) {
        remoteRepository?.deleteQuote(quote.id)
        dao.deleteQuote(quote.toEntity())
    }
}