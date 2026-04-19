package com.marianaalra.book.domain.usecase.book

import com.marianaalra.book.domain.model.Book
import com.marianaalra.book.domain.repository.BookRepository

class AddBookUseCase(private val repository: BookRepository) {

    suspend operator fun invoke(book: Book) {

        if (book.title.isBlank()) {
            throw IllegalArgumentException("El título del libro no puede estar vacío.")
        }

        if (book.fileUri.isNullOrBlank()) {
            throw IllegalArgumentException("El libro debe tener un archivo asociado.")
        }

        repository.insertBook(book)
    }
}