package com.marianaalra.book.data.repository.impl

import com.marianaalra.book.data.local.dao.NoteDao
import com.marianaalra.book.data.mapper.toDomain
import com.marianaalra.book.data.mapper.toEntity
import com.marianaalra.book.data.repository.remote.NoteRemoteRepository
import com.marianaalra.book.domain.model.NoteDomain
import com.marianaalra.book.domain.repository.NoteRepository
import com.marianaalra.book.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDao,
    private val remoteRepository: NoteRemoteRepository? = null
) : NoteRepository {

    override fun getNotesForBook(bookId: Long): Flow<List<NoteDomain>> {
        return dao.getNotesForBook(bookId).map { lista -> lista.map { it.toDomain() } }
    }

    override suspend fun insertNote(note: NoteDomain) {
        if (remoteRepository != null) {
            when (val remote = remoteRepository.createNote(note)) {
                is Resource.Success -> {
                    dao.insertNote(remote.data.toEntity())
                    return
                }
                else -> Unit
            }
        }
        dao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: NoteDomain) {
        if (remoteRepository != null) {
            when (val remote = remoteRepository.updateNote(note)) {
                is Resource.Success -> {
                    dao.updateNote(remote.data.toEntity())
                    return
                }
                else -> Unit
            }
        }
        dao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: NoteDomain) {
        remoteRepository?.deleteNote(note.id)
        dao.deleteNote(note.toEntity())
    }
}