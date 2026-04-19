package com.marianaalra.book.domain.usecase.quote

import com.marianaalra.book.domain.model.QuoteDomain
import com.marianaalra.book.domain.repository.QuoteRepository

class DeleteQuoteUseCase(private val repository: QuoteRepository) {
    suspend operator fun invoke(quote: QuoteDomain) {
        repository.deleteQuote(quote)
    }
}