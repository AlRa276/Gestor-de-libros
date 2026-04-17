package com.marianaalra.book.domain.usecase.book

import com.marianaalra.book.domain.model.Book
import com.marianaalra.book.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetBooksUseCase(private val repository: BookRepository) {
    // El operador 'invoke' permite llamar a esta clase como si fuera una función
    operator fun invoke(usuarioId: Long): Flow<List<Book>> {
        // Aquí podrías agregar lógica, como ordenar los libros alfabéticamente
        // Pero por ahora, solo le pedimos al repositorio que nos dé los datos
        return repository.getAllBooksForUser(usuarioId)
    }
}