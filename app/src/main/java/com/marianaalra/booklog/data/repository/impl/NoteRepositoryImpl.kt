package com.marianaalra.booklog.data.repository.impl

import com.marianaalra.booklog.data.local.dao.NoteDao
import com.marianaalra.booklog.data.mapper.toDomain
import com.marianaalra.booklog.data.mapper.toEntity
import com.marianaalra.booklog.domain.model.NoteDomain
import com.marianaalra.booklog.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {

    override fun getNotesForBook(bookId: Long): Flow<List<NoteDomain>> {
        return dao.getNotesForBook(bookId).map { lista -> lista.map { it.toDomain() } }
    }

    override suspend fun insertNote(note: NoteDomain) {
        dao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: NoteDomain) {
        dao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: NoteDomain) {
        dao.deleteNote(note.toEntity())
    }
}