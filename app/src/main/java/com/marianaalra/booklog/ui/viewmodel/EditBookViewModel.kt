package com.marianaalra.booklog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.model.ColeccionDomain
import com.marianaalra.booklog.domain.model.SerieDomain
import com.marianaalra.booklog.domain.repository.ColeccionRepository
import com.marianaalra.booklog.domain.repository.SerieRepository
import com.marianaalra.booklog.domain.usecase.book.UpdateBookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditBookViewModel(
    private val updateBookUseCase: UpdateBookUseCase,
    private val serieRepository: SerieRepository,
    private val coleccionRepository: ColeccionRepository
) : ViewModel() {

    private val _series = MutableStateFlow<List<SerieDomain>>(emptyList())
    val series: StateFlow<List<SerieDomain>> = _series

    private val _colecciones = MutableStateFlow<List<ColeccionDomain>>(emptyList())
    val colecciones: StateFlow<List<ColeccionDomain>> = _colecciones

    private val _coleccionesDelLibro = MutableStateFlow<List<ColeccionDomain>>(emptyList())
    val coleccionesDelLibro: StateFlow<List<ColeccionDomain>> = _coleccionesDelLibro

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadData(userId: Long, bookId: Long) {
        viewModelScope.launch {
            serieRepository.getSeriesForUser(userId).collect { _series.value = it }
        }
        viewModelScope.launch {
            coleccionRepository.getColeccionesForUser(userId).collect { _colecciones.value = it }
        }
        viewModelScope.launch {
            coleccionRepository.getColeccionesForBook(bookId).collect { _coleccionesDelLibro.value = it }
        }
    }

    fun saveBook(book: Book, selectedColecciones: List<ColeccionDomain>) {
        viewModelScope.launch {
            try {
                updateBookUseCase(book)
                coleccionRepository.setColeccionesForBook(book.id, selectedColecciones)
                _saved.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    suspend fun getOrCreateSerie(nombre: String, userId: Long): SerieDomain {
        val existing = serieRepository.searchSeries(userId, nombre)
            .firstOrNull { it.nombre.equals(nombre, ignoreCase = true) }
        if (existing != null) return existing
        val newId = serieRepository.insertSerie(SerieDomain(usuarioId = userId, nombre = nombre))
        return SerieDomain(id = newId, usuarioId = userId, nombre = nombre)
    }

    suspend fun getOrCreateColeccion(nombre: String, userId: Long): ColeccionDomain {
        val existing = coleccionRepository.searchColecciones(userId, nombre)
            .firstOrNull { it.nombre.equals(nombre, ignoreCase = true) }
        if (existing != null) return existing
        val newId = coleccionRepository.insertColeccion(ColeccionDomain(usuarioId = userId, nombre = nombre))
        return ColeccionDomain(id = newId, usuarioId = userId, nombre = nombre)
    }

    fun clearSaved() { _saved.value = false }
    fun clearError() { _error.value = null }
}