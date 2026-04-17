// BookLogApp.kt
package com.marianaalra.book

import android.app.Application
import com.marianaalra.book.data.local.db.AppDatabase
import com.marianaalra.book.data.remote.api.RetrofitClient
import com.marianaalra.book.data.repository.impl.AuthRepositoryImpl
import com.marianaalra.book.data.repository.impl.BookRepositoryImpl
import com.marianaalra.book.data.repository.impl.ColeccionRepositoryImpl
import com.marianaalra.book.data.repository.impl.NoteRepositoryImpl
import com.marianaalra.book.data.repository.impl.QuoteRepositoryImpl
import com.marianaalra.book.data.repository.impl.SerieRepositoryImpl
import com.marianaalra.book.data.repository.remote.BookRemoteRepository
import com.marianaalra.book.data.repository.remote.ColeccionRemoteRepository
import com.marianaalra.book.data.repository.remote.LecturaColeccionRemoteRepository
import com.marianaalra.book.data.repository.remote.NoteRemoteRepository
import com.marianaalra.book.data.repository.remote.QuoteRemoteRepository
import com.marianaalra.book.data.repository.remote.SerieRemoteRepository
import com.marianaalra.book.domain.usecase.auth.LoginUseCase
import com.marianaalra.book.domain.usecase.auth.LogoutUseCase
import com.marianaalra.book.domain.usecase.auth.RegisterUseCase
import com.marianaalra.book.domain.usecase.book.AddBookUseCase
import com.marianaalra.book.domain.usecase.book.GetBooksUseCase
import com.marianaalra.book.domain.usecase.book.UpdateBookUseCase
import com.marianaalra.book.domain.usecase.note.AddNoteUseCase
import com.marianaalra.book.domain.usecase.note.DeleteNoteUseCase
import com.marianaalra.book.domain.usecase.note.UpdateNoteUseCase
import com.marianaalra.book.domain.usecase.quote.AddQuoteUseCase
import com.marianaalra.book.domain.usecase.quote.DeleteQuoteUseCase
import com.marianaalra.book.domain.usecase.quote.GetQuotesUseCase
import com.marianaalra.book.domain.usecase.quote.UpdateQuoteUseCase

class BookLogApp : Application() {

    // La base de datos, creada una sola vez
    private val database by lazy { AppDatabase.getInstance(this) }

    // API remota
    private val apiService by lazy { RetrofitClient.apiService }

    // Repositorios remotos
    private val remoteBookRepository by lazy { BookRemoteRepository(apiService) }
    private val remoteNoteRepository by lazy { NoteRemoteRepository(apiService) }
    private val remoteQuoteRepository by lazy { QuoteRemoteRepository(apiService) }
    private val remoteSerieRepository by lazy { SerieRemoteRepository(apiService) }
    private val remoteColeccionRepository by lazy { ColeccionRemoteRepository(apiService) }
    private val remoteLecturaColeccionRepository by lazy { LecturaColeccionRemoteRepository(apiService) }

    // Repositorios
    val authRepository by lazy { AuthRepositoryImpl(database.userDao(), apiService) }
    val bookRepository by lazy { BookRepositoryImpl(database.bookDao(), remoteBookRepository) }
    val noteRepository by lazy { NoteRepositoryImpl(database.noteDao(), remoteNoteRepository) }
    val quoteRepository by lazy { QuoteRepositoryImpl(database.quoteDao(), remoteQuoteRepository) }

    // Use Cases de autenticación
    val loginUseCase by lazy { LoginUseCase(authRepository) }
    val registerUseCase by lazy { RegisterUseCase(authRepository) }
    val logoutUseCase by lazy { LogoutUseCase(authRepository) }

    // Use Cases de libros
    val getBooksUseCase by lazy { GetBooksUseCase(bookRepository) }
    val addBookUseCase by lazy { AddBookUseCase(bookRepository) }
    val updateBookUseCase by lazy { UpdateBookUseCase(bookRepository) }

    // Use Cases de notas
    val addNoteUseCase by lazy { AddNoteUseCase(noteRepository) }
    val updateNoteUseCase by lazy { UpdateNoteUseCase(noteRepository) }
    val deleteNoteUseCase by lazy { DeleteNoteUseCase(noteRepository) }

    // Use Cases de citas
    val getQuotesUseCase by lazy { GetQuotesUseCase(quoteRepository) }
    val addQuoteUseCase by lazy { AddQuoteUseCase(quoteRepository) }
    val updateQuoteUseCase by lazy { UpdateQuoteUseCase(quoteRepository) }
    val deleteQuoteUseCase by lazy { DeleteQuoteUseCase(quoteRepository) }
    val serieRepository by lazy { SerieRepositoryImpl(database.serieDao(), remoteSerieRepository) }
    val coleccionRepository by lazy {
        ColeccionRepositoryImpl(
            database.coleccionDao(),
            remoteColeccionRepository,
            remoteLecturaColeccionRepository
        )
    }
}