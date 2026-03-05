package com.marianaalra.booklog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marianaalra.booklog.domain.model.NoteDomain
import com.marianaalra.booklog.domain.model.QuoteDomain
import com.marianaalra.booklog.domain.usecase.note.AddNoteUseCase
import com.marianaalra.booklog.domain.usecase.note.DeleteNoteUseCase
import com.marianaalra.booklog.domain.usecase.note.UpdateNoteUseCase
import com.marianaalra.booklog.domain.usecase.quote.AddQuoteUseCase
import com.marianaalra.booklog.domain.usecase.quote.DeleteQuoteUseCase
import com.marianaalra.booklog.domain.usecase.quote.GetQuotesUseCase
import com.marianaalra.booklog.domain.usecase.quote.UpdateQuoteUseCase
import com.marianaalra.booklog.domain.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteRepository: NoteRepository,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getQuotesUseCase: GetQuotesUseCase,
    private val addQuoteUseCase: AddQuoteUseCase,
    private val updateQuoteUseCase: UpdateQuoteUseCase,
    private val deleteQuoteUseCase: DeleteQuoteUseCase
) : ViewModel() {

    private val _notes = MutableStateFlow<List<NoteDomain>>(emptyList())
    val notes: StateFlow<List<NoteDomain>> = _notes

    private val _quotes = MutableStateFlow<List<QuoteDomain>>(emptyList())
    val quotes: StateFlow<List<QuoteDomain>> = _quotes

    fun loadNotesAndQuotes(bookId: Long) {
        viewModelScope.launch {
            noteRepository.getNotesForBook(bookId).collect { _notes.value = it }
        }
        viewModelScope.launch {
            getQuotesUseCase(bookId).collect { _quotes.value = it }
        }
    }

    fun addNote(note: NoteDomain) {
        viewModelScope.launch { try { addNoteUseCase(note) } catch (e: Exception) { e.printStackTrace() } }
    }

    fun updateNote(note: NoteDomain) {
        viewModelScope.launch { try { updateNoteUseCase(note) } catch (e: Exception) { e.printStackTrace() } }
    }

    fun deleteNote(note: NoteDomain) {
        viewModelScope.launch { deleteNoteUseCase(note) }
    }

    fun addQuote(quote: QuoteDomain) {
        viewModelScope.launch { try { addQuoteUseCase(quote) } catch (e: Exception) { e.printStackTrace() } }
    }

    fun updateQuote(quote: QuoteDomain) {
        viewModelScope.launch { try { updateQuoteUseCase(quote) } catch (e: Exception) { e.printStackTrace() } }
    }

    fun deleteQuote(quote: QuoteDomain) {
        viewModelScope.launch { deleteQuoteUseCase(quote) }
    }
}
