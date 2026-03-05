package com.marianaalra.booklog.domain.usecase.note

import com.marianaalra.booklog.domain.model.NoteDomain
import com.marianaalra.booklog.domain.repository.NoteRepository

class UpdateNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: NoteDomain) {
        if (note.contenido.isBlank()) {
            throw Exception("La nota no puede estar vacía")
        }
        repository.updateNote(note)
    }
}
