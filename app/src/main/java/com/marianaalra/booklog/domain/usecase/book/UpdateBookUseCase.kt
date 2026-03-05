package com.marianaalra.booklog.domain.usecase.book

import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.repository.BookRepository

class UpdateBookUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(book: Book) {
        if (book.title.isBlank()) {
            throw Exception("El título no puede quedar vacío")
        }
        // Validamos que el estado sea uno de los 3 permitidos en la base de datos
        val validStatuses = listOf("PENDIENTE", "EN_PROGRESO", "FINALIZADA")
        if (!validStatuses.contains(book.status)) {
            throw Exception("Estado de lectura no válido")
        }
        repository.updateBook(book)
    }
}