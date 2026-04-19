package com.marianaalra.book.domain.usecase.quote

import com.marianaalra.book.domain.model.QuoteDomain
import com.marianaalra.book.domain.repository.QuoteRepository

class UpdateQuoteUseCase(private val repository: QuoteRepository) {
    suspend operator fun invoke(quote: QuoteDomain) {
        if (quote.textoCitado.isBlank()) {
            throw Exception("El texto citado no puede quedar vacío")
        }
        repository.updateQuote(quote)
    }
}
