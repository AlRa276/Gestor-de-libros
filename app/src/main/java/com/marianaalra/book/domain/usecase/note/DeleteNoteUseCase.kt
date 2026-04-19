package com.marianaalra.book.domain.usecase.note

import com.marianaalra.book.domain.model.NoteDomain
import com.marianaalra.book.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: NoteDomain) {
        repository.deleteNote(note)
    }
}
