package com.marianaalra.booklog.domain.usecase.quote

import com.marianaalra.booklog.domain.model.QuoteDomain
import com.marianaalra.booklog.domain.repository.QuoteRepository

class AddQuoteUseCase(private val repository: QuoteRepository) {
    suspend operator fun invoke(quote: QuoteDomain) {
        if (quote.textoCitado.isBlank()) {
            throw Exception("El texto citado es obligatorio")
        }
        repository.insertQuote(quote)
    }
}