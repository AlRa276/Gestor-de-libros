package com.marianaalra.book.domain.usecase.quote

import com.marianaalra.book.domain.model.QuoteDomain
import com.marianaalra.book.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow

class GetQuotesUseCase(private val repository: QuoteRepository) {
    operator fun invoke(bookId: Long): Flow<List<QuoteDomain>> {
        return repository.getQuotesForBook(bookId)
    }
}
