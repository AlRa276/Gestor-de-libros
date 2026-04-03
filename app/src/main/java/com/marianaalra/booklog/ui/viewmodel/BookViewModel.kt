package com.marianaalra.booklog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.model.SerieDomain
import com.marianaalra.booklog.domain.model.ColeccionDomain // 👈 1. Importa tu modelo de Colección
import com.marianaalra.booklog.domain.repository.SerieRepository
import com.marianaalra.booklog.domain.repository.ColeccionRepository // 👈 2. Importa el repositorio
import com.marianaalra.booklog.domain.usecase.book.AddBookUseCase
import com.marianaalra.booklog.domain.usecase.book.GetBooksUseCase
import com.marianaalra.booklog.domain.usecase.book.UpdateBookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow // 👈 Para la función de filtrado
import kotlinx.coroutines.launch

class BookViewModel(
    private val getBooksUseCase: GetBooksUseCase,
    private val addBookUseCase: AddBookUseCase,
    private val updateBookUseCase: UpdateBookUseCase,
    private val serieRepository: SerieRepository,
    private val coleccionRepository: ColeccionRepository // 👈 3. Inyectamos el repositorio de colecciones
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _series = MutableStateFlow<List<SerieDomain>>(emptyList())
    val series: StateFlow<List<SerieDomain>> = _series

    // --- ESTADO PARA COLECCIONES ---
    private val _colecciones = MutableStateFlow<List<ColeccionDomain>>(emptyList()) // 👈 4. Estado de colecciones
    val colecciones: StateFlow<List<ColeccionDomain>> = _colecciones

    fun loadBooks(usuarioId: Long) {
        viewModelScope.launch {
            getBooksUseCase(usuarioId).collect { lista ->
                _books.value = lista
            }
        }
    }

    fun loadSeries(usuarioId: Long) {
        viewModelScope.launch {
            serieRepository.getSeriesForUser(usuarioId).collect { listaSeries ->
                _series.value = listaSeries
            }
        }
    }

    // --- CARGAR COLECCIONES ---
    fun loadColecciones(usuarioId: Long) { // 👈 5. Función para cargar etiquetas
        viewModelScope.launch {
            coleccionRepository.getColeccionesForUser(usuarioId).collect { lista ->
                _colecciones.value = lista
            }
        }
    }

    // --- OBTENER LIBROS POR COLECCIÓN ---
    // Esta función la usaremos en la UI para filtrar dinámicamente
    fun getBooksByColeccion(coleccionId: Long): Flow<List<Book>> { // 👈 6. Filtrado reactivo
        return coleccionRepository.getBooksForColeccion(coleccionId)
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            try {
                android.util.Log.d("BookViewModel", "Guardando libro: ${book.title}, usuarioId: ${book.usuarioId}")
                addBookUseCase(book)
            } catch (e: Exception) {
                android.util.Log.e("BookViewModel", "Error al guardar: ${e.message}", e)
            }
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            try { updateBookUseCase(book) } catch (e: Exception) { e.printStackTrace() }
        }
    }

    // Estado de navegación interna del drawer
    private val _selectedSection = MutableStateFlow("Biblioteca")
    val selectedSection: StateFlow<String> = _selectedSection

    private val _selectedAuthor = MutableStateFlow<String?>(null)
    val selectedAuthor: StateFlow<String?> = _selectedAuthor

    private val _selectedSerieId = MutableStateFlow<Long?>(null)
    val selectedSerieId: StateFlow<Long?> = _selectedSerieId

    private val _selectedColeccionId = MutableStateFlow<Long?>(null)
    val selectedColeccionId: StateFlow<Long?> = _selectedColeccionId

    fun setSelectedSection(section: String) {
        _selectedSection.value = section
        _selectedAuthor.value = null
        _selectedSerieId.value = null
        _selectedColeccionId.value = null
    }

    fun setSelectedAuthor(author: String?) { _selectedAuthor.value = author }
    fun setSelectedSerieId(id: Long?) { _selectedSerieId.value = id }
    fun setSelectedColeccionId(id: Long?) { _selectedColeccionId.value = id }
}