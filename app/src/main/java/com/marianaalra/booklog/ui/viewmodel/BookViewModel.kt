package com.marianaalra.booklog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.usecase.book.AddBookUseCase
import com.marianaalra.booklog.domain.usecase.book.GetBooksUseCase
import com.marianaalra.booklog.domain.usecase.book.UpdateBookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val getBooksUseCase: GetBooksUseCase,
    private val addBookUseCase: AddBookUseCase,
    private val updateBookUseCase: UpdateBookUseCase
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    fun loadBooks(usuarioId: Long) {
        viewModelScope.launch {
            getBooksUseCase(usuarioId).collect { lista ->
                _books.value = lista
            }
        }
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            try { addBookUseCase(book) } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            try { updateBookUseCase(book) } catch (e: Exception) { e.printStackTrace() }
        }
    }
}