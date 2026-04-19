package com.marianaalra.book.domain.usecase.note

import com.marianaalra.book.domain.model.NoteDomain
import com.marianaalra.book.domain.repository.NoteRepository

class UpdateNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: NoteDomain) {
        if (note.contenido.isBlank()) {
            throw Exception("La nota no puede estar vacía")
        }
        repository.updateNote(note)
    }
}
