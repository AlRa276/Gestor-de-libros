package com.marianaalra.book.data.repository.impl

import com.marianaalra.book.data.local.dao.ColeccionDao
import com.marianaalra.book.data.local.entity.LecturaColeccionEntity
import com.marianaalra.book.data.mapper.toDomain
import com.marianaalra.book.data.mapper.toEntity
import com.marianaalra.book.data.repository.remote.ColeccionRemoteRepository
import com.marianaalra.book.data.repository.remote.LecturaColeccionRemoteRepository
import com.marianaalra.book.domain.model.Book
import com.marianaalra.book.domain.model.ColeccionDomain
import com.marianaalra.book.domain.repository.ColeccionRepository
import com.marianaalra.book.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ColeccionRepositoryImpl(
    private val dao: ColeccionDao,
    private val remoteRepository: ColeccionRemoteRepository? = null,
    private val lecturaColeccionRemoteRepository: LecturaColeccionRemoteRepository? = null
) : ColeccionRepository {
    override fun getColeccionesForUser(userId: Long) =
        dao.getColeccionesForUser(userId).map { it.map { e -> e.toDomain() } }

    override suspend fun searchColecciones(userId: Long, query: String) =
        dao.searchColecciones(userId, query).map { it.toDomain() }

    override fun getColeccionesForBook(bookId: Long) =
        dao.getColeccionesForBook(bookId).map { it.map { e -> e.toDomain() } }

    override suspend fun setColeccionesForBook(bookId: Long, colecciones: List<ColeccionDomain>) {
        dao.deleteAllColeccionesForBook(bookId)
        colecciones.forEach {
            dao.insertLecturaColeccion(LecturaColeccionEntity(lecturaId = bookId, coleccionId = it.id))
            lecturaColeccionRemoteRepository?.addBookToColeccion(bookId, it.id)
        }
    }

    override suspend fun insertColeccion(coleccion: ColeccionDomain): Long {
        if (remoteRepository != null) {
            when (val remote = remoteRepository.createColeccion(coleccion)) {
                is Resource.Success -> {
                    val saved = remote.data
                    dao.insertColeccion(saved.toEntity())
                    return saved.id
                }
                else -> Unit
            }
        }
        return dao.insertColeccion(coleccion.toEntity())
    }

    override suspend fun deleteColeccion(coleccion: ColeccionDomain) {
        remoteRepository?.deleteColeccion(coleccion.id)
        dao.deleteColeccion(coleccion.toEntity())
    }

    // ... dentro de ColeccionRepositoryImpl

    override fun getBooksForColeccion(coleccionId: Long): Flow<List<Book>> {
        return dao.getBooksForColeccion(coleccionId).map { entities ->
            entities.map { it.toDomain() } // Usamos el mapper que ya tienes para Book
        }
    }

}