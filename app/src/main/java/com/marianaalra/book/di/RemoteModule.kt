package com.marianaalra.book.di

import com.marianaalra.book.data.local.dao.UserDao
import com.marianaalra.book.data.remote.api.ApiService
import com.marianaalra.book.data.remote.api.RetrofitClient
import com.marianaalra.book.data.repository.impl.AuthRemoteRepositoryImpl
import com.marianaalra.book.data.repository.remote.BookRemoteRepository
import com.marianaalra.book.data.repository.remote.ColeccionRemoteRepository
import com.marianaalra.book.data.repository.remote.LecturaColeccionRemoteRepository
import com.marianaalra.book.data.repository.remote.NoteRemoteRepository
import com.marianaalra.book.data.repository.remote.QuoteRemoteRepository
import com.marianaalra.book.data.repository.remote.SerieRemoteRepository

/**
 * MÓDULO DE INYECCIÓN DE DEPENDENCIAS
 *
 * Este módulo provee todas las instancias necesarias para usar la API remota.
 *
 * Puedes usar esto con:
 * - Hilt (recomendado para Android moderno)
 * - Koin
 * - Dagger
 * - Manual (como se muestra aquí)
 */

object RemoteModule {

    /**
     * Obtener la instancia de ApiService (singleton de Retrofit)
     */
    fun provideApiService(): ApiService {
        return RetrofitClient.apiService
    }

    /**
     * Proveer AuthRemoteRepository (login/registro)
     */
    fun provideAuthRemoteRepository(
        apiService: ApiService,
        userDao: UserDao
    ): AuthRemoteRepositoryImpl {
        return AuthRemoteRepositoryImpl(apiService, userDao)
    }

    /**
     * Proveer BookRemoteRepository
     */
    fun provideBookRemoteRepository(
        apiService: ApiService = provideApiService()
    ): BookRemoteRepository {
        return BookRemoteRepository(apiService)
    }

    /**
     * Proveer SerieRemoteRepository
     */
    fun provideSerieRemoteRepository(
        apiService: ApiService = provideApiService()
    ): SerieRemoteRepository {
        return SerieRemoteRepository(apiService)
    }

    /**
     * Proveer ColeccionRemoteRepository
     */
    fun provideColeccionRemoteRepository(
        apiService: ApiService = provideApiService()
    ): ColeccionRemoteRepository {
        return ColeccionRemoteRepository(apiService)
    }

    /**
     * Proveer NoteRemoteRepository
     */
    fun provideNoteRemoteRepository(
        apiService: ApiService = provideApiService()
    ): NoteRemoteRepository {
        return NoteRemoteRepository(apiService)
    }

    /**
     * Proveer QuoteRemoteRepository
     */
    fun provideQuoteRemoteRepository(
        apiService: ApiService = provideApiService()
    ): QuoteRemoteRepository {
        return QuoteRemoteRepository(apiService)
    }

    /**
     * Proveer LecturaColeccionRemoteRepository
     */
    fun provideLecturaColeccionRemoteRepository(
        apiService: ApiService = provideApiService()
    ): LecturaColeccionRemoteRepository {
        return LecturaColeccionRemoteRepository(apiService)
    }
}

/**
 * EJEMPLO DE USO (en tu Activity/Fragment):
 *
 * val authRemote = RemoteModule.provideAuthRemoteRepository(
 *     apiService = RemoteModule.provideApiService(),
 *     userDao = database.userDao()
 * )
 *
 * val bookRemote = RemoteModule.provideBookRemoteRepository()
 *
 * // En tu ViewModel:
 * class MyViewModel(
 *     private val authRemote: AuthRemoteRepositoryImpl,
 *     private val bookRemote: BookRemoteRepository
 * ) : ViewModel() {
 *     // ... tu código
 * }
 */