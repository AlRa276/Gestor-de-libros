package com.marianaalra.booklog.data.repository.impl

import com.marianaalra.booklog.data.local.dao.BookDao
import com.marianaalra.booklog.data.mapper.toDomain
import com.marianaalra.booklog.data.mapper.toEntity
import com.marianaalra.booklog.data.repository.remote.BookRemoteRepository
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.repository.BookRepository
import com.marianaalra.booklog.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(
    private val dao: BookDao,
    private val remoteRepository: BookRemoteRepository? = null
) : BookRepository {

    override fun getAllBooksForUser(usuarioId: Long): Flow<List<Book>> {
        // Obtenemos los Entity de Room y los convertimos a Modelos de Dominio
        return dao.getAllBooksForUser(usuarioId).map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun getBookById(bookId: Long): Book? {
        if (remoteRepository != null) {
            when (val remote = remoteRepository.getBookById(bookId)) {
                is Resource.Success -> {
                    dao.insertBook(remote.data.toEntity())
                    return remote.data
                }
                else -> Unit
            }
        }
        return dao.getBookById(bookId)?.toDomain()
    }

    override suspend fun insertBook(book: Book) {
        if (remoteRepository != null) {
            when (val remote = remoteRepository.createBook(book)) {
                is Resource.Success -> {
                    dao.insertBook(remote.data.toEntity())
                    return
                }
                else -> Unit
            }
        }
        dao.insertBook(book.toEntity())
    }

    override suspend fun updateBook(book: Book) {
        if (remoteRepository != null) {
            when (val remote = remoteRepository.updateBook(book)) {
                is Resource.Success -> {
                    dao.updateBook(remote.data.toEntity())
                    return
                }
                else -> Unit
            }
        }
        dao.updateBook(book.toEntity())
    }

    override suspend fun deleteBook(book: Book) {
        remoteRepository?.deleteBook(book.id)
        dao.deleteBook(book.toEntity())
    }
}