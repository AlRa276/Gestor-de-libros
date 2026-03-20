
package com.marianaalra.booklog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marianaalra.booklog.BookLogApp

class ViewModelFactory(private val app: BookLogApp) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(app.loginUseCase, app.registerUseCase, app.logoutUseCase) as T

            // ... dentro del when de tu ViewModelFactory

            modelClass.isAssignableFrom(BookViewModel::class.java) ->
                BookViewModel(
                    app.getBooksUseCase,
                    app.addBookUseCase,
                    app.updateBookUseCase,
                    app.serieRepository,
                    app.coleccionRepository// 👈 Agregamos esta línea que faltaba
                ) as T

// ... el resto sigue igual

            modelClass.isAssignableFrom(NotesViewModel::class.java) ->
                NotesViewModel(
                    app.noteRepository,
                    app.addNoteUseCase, app.updateNoteUseCase, app.deleteNoteUseCase,
                    app.getQuotesUseCase, app.addQuoteUseCase, app.updateQuoteUseCase, app.deleteQuoteUseCase
                ) as T

            modelClass.isAssignableFrom(EditBookViewModel::class.java) ->
                EditBookViewModel(
                    app.updateBookUseCase,
                    app.serieRepository,
                    app.coleccionRepository
                ) as T

            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}