package com.marianaalra.book.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marianaalra.book.BookLogApp

class ViewModelFactory(private val app: BookLogApp) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            // 1. Autenticación
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(app.loginUseCase, app.registerUseCase, app.logoutUseCase) as T

            // 2. Listado de librettos
            modelClass.isAssignableFrom(BookViewModel::class.java) ->
                BookViewModel(
                    app.getBooksUseCase,
                    app.addBookUseCase,
                    app.updateBookUseCase,
                    app.serieRepository,
                    app.coleccionRepository
                ) as T

            // 3. Notas y Citas
            modelClass.isAssignableFrom(NotesViewModel::class.java) ->
                NotesViewModel(
                    app.noteRepository,
                    app.addNoteUseCase,
                    app.updateNoteUseCase,
                    app.deleteNoteUseCase,
                    app.getQuotesUseCase,
                    app.addQuoteUseCase,
                    app.updateQuoteUseCase,
                    app.deleteQuoteUseCase
                ) as T

            // 4. Edición de libros
            modelClass.isAssignableFrom(EditBookViewModel::class.java) ->
                EditBookViewModel(
                    app.updateBookUseCase,
                    app.serieRepository,
                    app.coleccionRepository
                ) as T

            // 5. Estadísticas (LO QUE FALTABA)
            modelClass.isAssignableFrom(StatisticsViewModel::class.java) ->
                StatisticsViewModel(
                    app.getBooksUseCase
                ) as T

            // Error de seguridad si olvidas registrar uno nuevo en el futuro
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}