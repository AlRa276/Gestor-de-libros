package com.marianaalra.booklog.domain.usecase.quote

import com.marianaalra.booklog.domain.model.QuoteDomain
import com.marianaalra.booklog.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow

class GetQuotesUseCase(private val repository: QuoteRepository) {
    operator fun invoke(bookId: Long): Flow<List<QuoteDomain>> {
        return repository.getQuotesForBook(bookId)
    }
}
