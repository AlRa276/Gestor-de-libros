package com.marianaalra.book.example

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marianaalra.book.data.repository.impl.AuthRemoteRepositoryImpl
import com.marianaalra.book.data.repository.remote.BookRemoteRepository
import com.marianaalra.book.domain.model.Book
import com.marianaalra.book.domain.model.UserDomain
import com.marianaalra.book.domain.util.Resource
import kotlinx.coroutines.launch
import com.marianaalra.book.data.local.dao.UserDao

/**
 * EJEMPLO COMPLETO: Cómo integrar los Repositorios Remote en tu ViewModel
 *
 * Este archivo muestra:
 * 1. Login remoto con AuthRemoteRepositoryImpl
 * 2. Obtener libros del usuario con BookRemoteRepository
 * 3. Crear nuevo libro
 * 4. Manejo de Resource<T> para estados (Success, Error, Loading)
 */

class ExampleBookViewModel(
    private val authRemote: AuthRemoteRepositoryImpl,
    private val bookRemote: BookRemoteRepository,
    private val userDao: UserDao
) : ViewModel() {

    // Estados de UI (en tu app usarías StateFlow o LiveData)
    private var currentUser: UserDomain? = null
    private var books: List<Book> = emptyList()
    private var isLoading = false

    // ==================== LOGIN REMOTO ====================

    fun loginRemoto(correo: String, contrasena: String) {
        viewModelScope.launch {
            isLoading = true

            val result = authRemote.loginRemote(correo, contrasena)

            when (result) {
                is Resource.Success -> {
                    currentUser = result.data
                    Log.d("BookVM", "Login exitoso: ${currentUser?.nombreUsuario}")
                    // Aquí podrías navegar a la pantalla principal
                    loadBooks()
                }
                is Resource.Error -> {
                    Log.e("BookVM", "Error login: ${result.exception.message}")
                    // mostrarError(result.exception.message)
                }
                is Resource.Loading -> {
                    Log.d("BookVM", "Logging in...")
                }
            }

            isLoading = false
        }
    }

    // ==================== REGISTRO REMOTO ====================

    fun registroRemoto(nombreUsuario: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            isLoading = true

            val result = authRemote.registerRemote(nombreUsuario, correo, contrasena)

            when (result) {
                is Resource.Success -> {
                    currentUser = result.data
                    Log.d("BookVM", "Registro exitoso: ${currentUser?.nombreUsuario}")
                    loadBooks()
                }
                is Resource.Error -> {
                    Log.e("BookVM", "Error registro: ${result.exception.message}")
                }
                is Resource.Loading -> {}
            }

            isLoading = false
        }
    }

    // ==================== OBTENER LIBROS ====================

    fun loadBooks() {
        if (currentUser == null) {
            Log.e("BookVM", "Usuario no autenticado")
            return
        }

        viewModelScope.launch {
            isLoading = true

            val result = bookRemote.getBooksByUsuarioId(currentUser!!.id)

            when (result) {
                is Resource.Success -> {
                    books = result.data
                    Log.d("BookVM", "Se cargaron ${books.size} libros")
                    // Aquí actualizarías tu UI con los libros
                }
                is Resource.Error -> {
                    Log.e("BookVM", "Error al cargar libros: ${result.exception.message}")
                }
                is Resource.Loading -> {
                    Log.d("BookVM", "Cargando libros...")
                }
            }

            isLoading = false
        }
    }

    // ==================== OBTENER LIBROS POR ESTADO ====================

    fun loadBooksByEstado(estado: String) {
        if (currentUser == null) return

        viewModelScope.launch {
            val result = bookRemote.getBooksByEstado(currentUser!!.id, estado)

            when (result) {
                is Resource.Success -> {
                    books = result.data
                    Log.d("BookVM", "Libros con estado '$estado': ${books.size}")
                }
                is Resource.Error -> {
                    Log.e("BookVM", "Error: ${result.exception.message}")
                }
                is Resource.Loading -> {}
            }
        }
    }

    // ==================== BUSCAR LIBROS ====================

    fun searchBooks(titulo: String) {
        if (currentUser == null) return

        viewModelScope.launch {
            val result = bookRemote.searchByTitulo(currentUser!!.id, titulo)

            when (result) {
                is Resource.Success -> {
                    books = result.data
                    Log.d("BookVM", "Búsqueda encontró ${books.size} resultados")
                }
                is Resource.Error -> {
                    Log.e("BookVM", "Error búsqueda: ${result.exception.message}")
                }
                is Resource.Loading -> {}
            }
        }
    }

    // ==================== CREAR LIBRO ====================

    fun crearLibro(
        titulo: String,
        autor: String,
        formato: String,
        rutaArchivo: String,
        nombreArchivo: String
    ) {
        if (currentUser == null) return

        viewModelScope.launch {
            // Crear objeto Book con los datos
            val nuevoLibro = Book(
                usuarioId = currentUser!!.id,
                title = titulo,
                author = autor,
                fileFormat = formato,
                fileUri = rutaArchivo,
                nombreArchivo = nombreArchivo,
                status = "PENDIENTE",
                progress = 0f
            )

            val result = bookRemote.createBook(nuevoLibro)

            when (result) {
                is Resource.Success -> {
                    val libroCreado = result.data
                    Log.d("BookVM", "Libro creado con ID: ${libroCreado.id}")
                    // Recargar lista de libros
                    loadBooks()
                }
                is Resource.Error -> {
                    Log.e("BookVM", "Error al crear libro: ${result.exception.message}")
                }
                is Resource.Loading -> {}
            }
        }
    }

    // ==================== ACTUALIZAR PROGRESO ====================

    fun actualizarProgreso(bookId: Long, progreso: Float) {
        viewModelScope.launch {
            val result = bookRemote.updateBookProgress(bookId, progreso)

            when (result) {
                is Resource.Success -> {
                    Log.d("BookVM", "Progreso actualizado: $progreso%")
                    // Actualizar en lista local
                    books = books.map {
                        if (it.id == bookId) it.copy(progress = progreso) else it
                    }
                }
                is Resource.Error -> {
                    Log.e("BookVM", "Error: ${result.exception.message}")
                }
                is Resource.Loading -> {}
            }
        }
    }

    // ==================== ELIMINAR LIBRO ====================

    fun eliminarLibro(bookId: Long) {
        viewModelScope.launch {
            val result = bookRemote.deleteBook(bookId)

            when (result) {
                is Resource.Success -> {
                    Log.d("BookVM", "Libro eliminado")
                    // Recargar lista
                    loadBooks()
                }
                is Resource.Error -> {
                    Log.e("BookVM", "Error: ${result.exception.message}")
                }
                is Resource.Loading -> {}
            }
        }
    }

    // ==================== LOGOUT ====================

    fun logout() {
        viewModelScope.launch {
            authRemote.logout()
            currentUser = null
            books = emptyList()
            Log.d("BookVM", "Logout completado")
        }
    }

    // ==================== GETTERS ====================

    fun getCurrentUser() = currentUser
    fun getBooks() = books
    fun isLoading() = isLoading
}