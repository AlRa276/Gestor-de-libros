package com.marianaalra.booklog.domain.repository

import com.marianaalra.booklog.domain.model.NoteDomain
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    // Trae todas las notas, pero solo de un libro en específico
    fun getNotesForBook(bookId: Long): Flow<List<NoteDomain>>

    suspend fun insertNote(note: NoteDomain)

    suspend fun updateNote(note: NoteDomain)

    suspend fun deleteNote(note: NoteDomain)
}