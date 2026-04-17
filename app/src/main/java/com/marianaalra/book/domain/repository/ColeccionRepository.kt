package com.marianaalra.book.domain.repository

import com.marianaalra.book.domain.model.Book
import com.marianaalra.book.domain.model.ColeccionDomain
import kotlinx.coroutines.flow.Flow

interface ColeccionRepository {
    fun getColeccionesForUser(userId: Long): Flow<List<ColeccionDomain>>
    suspend fun searchColecciones(userId: Long, query: String): List<ColeccionDomain>
    fun getColeccionesForBook(bookId: Long): Flow<List<ColeccionDomain>>
    suspend fun setColeccionesForBook(bookId: Long, colecciones: List<ColeccionDomain>)
    suspend fun insertColeccion(coleccion: ColeccionDomain): Long
    suspend fun deleteColeccion(coleccion: ColeccionDomain)

    // ... dentro de interface ColeccionRepository
    fun getBooksForColeccion(coleccionId: Long): Flow<List<Book>>

}