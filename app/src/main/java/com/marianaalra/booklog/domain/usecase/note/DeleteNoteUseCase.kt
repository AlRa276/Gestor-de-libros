package com.marianaalra.booklog.domain.usecase.note

import com.marianaalra.booklog.domain.model.NoteDomain
import com.marianaalra.booklog.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: NoteDomain) {
        repository.deleteNote(note)
    }
}
