package com.marianaalra.booklog.domain.repository

import com.marianaalra.booklog.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    // Usamos Flow para que la pantalla se actualice sola si hay cambios
    fun getAllBooksForUser(usuarioId: Long): Flow<List<Book>>

    suspend fun getBookById(bookId: Long): Book?

    suspend fun insertBook(book: Book)

    suspend fun updateBook(book: Book)

    suspend fun deleteBook(book: Book)
}