package com.marianaalra.booklog.data.repository.impl

import com.marianaalra.booklog.data.local.dao.BookDao
import com.marianaalra.booklog.data.mapper.toDomain
import com.marianaalra.booklog.data.mapper.toEntity
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(private val dao: BookDao) : BookRepository {

    override fun getAllBooksForUser(usuarioId: Long): Flow<List<Book>> {
        // Obtenemos los Entity de Room y los convertimos a Modelos de Dominio
        return dao.getAllBooksForUser(usuarioId).map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun getBookById(bookId: Long): Book? {
        return dao.getBookById(bookId)?.toDomain()
    }

    override suspend fun insertBook(book: Book) {
        dao.insertBook(book.toEntity())
    }

    override suspend fun updateBook(book: Book) {
        dao.updateBook(book.toEntity())
    }

    override suspend fun deleteBook(book: Book) {
        dao.deleteBook(book.toEntity())
    }
}