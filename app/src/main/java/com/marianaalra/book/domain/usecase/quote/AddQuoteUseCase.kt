package com.marianaalra.book.domain.usecase.quote

import com.marianaalra.book.domain.model.QuoteDomain
import com.marianaalra.book.domain.repository.QuoteRepository

class AddQuoteUseCase(private val repository: QuoteRepository) {
    suspend operator fun invoke(quote: QuoteDomain) {
        if (quote.textoCitado.isBlank()) {
            throw Exception("El texto citado es obligatorio")
        }
        repository.insertQuote(quote)
    }
}