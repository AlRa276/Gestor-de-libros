package com.marianaalra.booklog.domain.usecase.quote

import com.marianaalra.booklog.domain.model.QuoteDomain
import com.marianaalra.booklog.domain.repository.QuoteRepository

class DeleteQuoteUseCase(private val repository: QuoteRepository) {
    suspend operator fun invoke(quote: QuoteDomain) {
        repository.deleteQuote(quote)
    }
}