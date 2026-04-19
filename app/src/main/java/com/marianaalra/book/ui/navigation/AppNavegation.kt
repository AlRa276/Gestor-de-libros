// AppNavegation.kt
package com.marianaalra.book.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marianaalra.book.ui.feature.auth.LoginScreen
import com.marianaalra.book.ui.feature.auth.RegisterScreen
import com.marianaalra.book.ui.feature.library.MainScreenWithDrawer
import java.net.URLDecoder
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marianaalra.book.BookLogApp
import com.marianaalra.book.domain.model.NoteDomain
import com.marianaalra.book.domain.model.QuoteDomain
import com.marianaalra.book.ui.feature.library.EditBookScreen
import com.marianaalra.book.ui.feature.notes.NotesAndQuotesScreen
import com.marianaalra.book.ui.feature.reading.ReadingScreen
import com.marianaalra.book.ui.feature.statistics.StatisticsScreen
import com.marianaalra.book.ui.viewmodel.AuthViewModel
import com.marianaalra.book.ui.viewmodel.BookViewModel
import com.marianaalra.book.ui.viewmodel.EditBookViewModel
import com.marianaalra.book.ui.viewmodel.NotesViewModel
import com.marianaalra.book.ui.viewmodel.StatisticsViewModel
import com.marianaalra.book.ui.viewmodel.ViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as BookLogApp
    val factory = ViewModelFactory(app)

    // ViewModels compartidos entre pantallas
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val bookViewModel: BookViewModel = viewModel(factory = factory)
    val notesViewModel: NotesViewModel = viewModel(factory = factory)
    val editBookViewModel: EditBookViewModel = viewModel(factory = factory)
    val statisticsViewModel: StatisticsViewModel = viewModel(factory = factory)

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            val error by authViewModel.error.collectAsState()
            LaunchedEffect(Unit) {
                authViewModel.clearError() //  LIMPIA AL ENTRAR
            }
            LoginScreen(
                onLoginClick = { correo, contrasena ->
                    authViewModel.login(correo, contrasena) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                errorMessage = error,
                onErrorShown = { authViewModel.clearError() }
            )
        }

        composable(Screen.Register.route) {
            val error by authViewModel.error.collectAsState()
            LaunchedEffect(Unit) {
                authViewModel.clearError() //  LIMPIA AL ENTRAR
            }
            RegisterScreen(
                onRegisterClick = { username, email, password ->
                    authViewModel.register(username, email, password) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() },
                errorMessage = error,
                onErrorShown = { authViewModel.clearError() }
            )
        }

        //Version1
        composable(Screen.Home.route) {
            val currentUser by authViewModel.currentUser.collectAsState()

            // 👇 Mostramos un loading mientras el usuario carga
            // en vez de redirigir inmediatamente
            if (currentUser == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@composable
            }

            MainScreenWithDrawer(
                bookViewModel = bookViewModel,
                usuarioId = currentUser!!.id,
                onNavigateToReading = { bookTitle, fileUri ->
                    val book = bookViewModel.books.value.find { it.title == bookTitle }
                    navController.navigate(Screen.Reading.createRoute(bookTitle, book?.id ?: 0L, fileUri))
                },
                onNavigateToNotes = { bookTitle, bookId ->
                    navController.navigate(Screen.Notes.createRoute(bookTitle, bookId))
                },
                onNavigateToEdit = { bookId: Long ->
                    navController.navigate(Screen.EditBook.createRoute(bookId))
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onLogout = {
                    authViewModel.logout {
                        navController.navigate(Screen.Login.route) { popUpTo(0) }
                    }
                }
            )
        }

        composable(Screen.Reading.route) { backStackEntry ->
            val rawTitle = backStackEntry.arguments?.getString("bookTitle") ?: "Libro"
            val title = URLDecoder.decode(rawTitle, "UTF-8")
            val encodedUri = backStackEntry.arguments?.getString("fileUri")
            val fileUri = URLDecoder.decode(encodedUri, "UTF-8").ifBlank { null }
            val bookId = backStackEntry.arguments?.getString("bookId")?.toLongOrNull() ?: 0L

            val books by bookViewModel.books.collectAsState()
            val currentBook = books.find { it.id == bookId }  // 👈 NUEVO

            LaunchedEffect(currentBook) {
                currentBook?.let { book ->
                    if (book.status == "PENDIENTE") {
                        bookViewModel.updateBook(book.copy(status = "EN_PROGRESO"))
                    }
                }
            }

            ReadingScreen(
                bookTitle = title,
                fileUriString = fileUri,
                bookId = bookId,
                onNavigateToEdit = { bookId: Long ->
                    navController.navigate(Screen.EditBook.createRoute(bookId))
                },
                onNavigateToNotes = { bookTitle, bookId ->
                    navController.navigate(Screen.Notes.createRoute(bookTitle, bookId))
                },
                onBack = {
                    navController.popBackStack()
                },
                currentBook = currentBook,
                notesViewModel = notesViewModel,
                bookViewModel = bookViewModel,
                initialProgress = currentBook?.progress ?: 0f,
                onNavigateBack = { navController.popBackStack() },
                onAddNote = { },
                onAddCitation = { }
            )
        }
        composable(Screen.Notes.route) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("bookTitle") ?: "Libro"
            val bookId = backStackEntry.arguments?.getString("bookId")?.toLongOrNull() ?: 0L

            val notes by notesViewModel.notes.collectAsState()
            val quotes by notesViewModel.quotes.collectAsState()

            // Estado para los diálogos de edición
            val noteToEditState = remember { mutableStateOf<NoteDomain?>(null) }
            val quoteToEditState = remember { mutableStateOf<QuoteDomain?>(null) }

            LaunchedEffect(bookId) {
                notesViewModel.loadNotesAndQuotes(bookId)
            }

            // Diálogo editar nota
            noteToEditState.value?.let { note ->
                val editedContentState = remember { mutableStateOf(note.contenido) }
                val editedPageState = remember { mutableStateOf(note.referenciaPagina ?: "") }
                AlertDialog(
                    onDismissRequest = { noteToEditState.value = null },
                    title = { Text("Editar nota") },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = editedContentState.value,
                                onValueChange = { editedContentState.value = it },
                                label = { Text("Contenido") },
                                modifier = Modifier.fillMaxWidth().height(120.dp),
                                maxLines = 5
                            )
                            OutlinedTextField(
                                value = editedPageState.value,
                                onValueChange = { editedPageState.value = it },
                                label = { Text("Página (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            notesViewModel.updateNote(note.copy(
                                contenido = editedContentState.value,
                                referenciaPagina = editedPageState.value.ifBlank { null }
                            ))
                            noteToEditState.value = null
                        }) { Text("Guardar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { noteToEditState.value = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            // Diálogo editar cita
            quoteToEditState.value?.let { quote ->
                val editedTextState = remember { mutableStateOf(quote.textoCitado) }
                val editedCommentState = remember { mutableStateOf(quote.comentario ?: "") }
                val editedPageState = remember { mutableStateOf(quote.referenciaPagina ?: "") }
                AlertDialog(
                    onDismissRequest = { quoteToEditState.value = null },
                    title = { Text("Editar cita") },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = editedTextState.value,
                                onValueChange = { editedTextState.value = it },
                                label = { Text("Texto citado") },
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                maxLines = 4
                            )
                            OutlinedTextField(
                                value = editedCommentState.value,
                                onValueChange = { editedCommentState.value = it },
                                label = { Text("Comentario (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = editedPageState.value,
                                onValueChange = { editedPageState.value = it },
                                label = { Text("Página (opcional)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            notesViewModel.updateQuote(quote.copy(
                                textoCitado = editedTextState.value,
                                comentario = editedCommentState.value.ifBlank { null },
                                referenciaPagina = editedPageState.value.ifBlank { null }
                            ))
                            quoteToEditState.value = null
                        }) { Text("Guardar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { quoteToEditState.value = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            NotesAndQuotesScreen(
                bookTitle = title,
                notes = notes,
                quotes = quotes,
                onNavigateBack = { navController.popBackStack() },
                onEditNote = { noteToEditState.value = it },
                onDeleteNote = { notesViewModel.deleteNote(it) },
                onEditQuote = { quoteToEditState.value = it },
                onDeleteQuote = { notesViewModel.deleteQuote(it) }
            )
        }

        composable(Screen.EditBook.route) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")?.toLongOrNull() ?: 0L
            val books by bookViewModel.books.collectAsState()
            val currentUser by authViewModel.currentUser.collectAsState()
            val book = books.find { it.id == bookId }

            if (book != null && currentUser != null) {
                EditBookScreen(
                    book = book,
                    userId = currentUser!!.id,
                    editBookViewModel = editBookViewModel,
                    onNavigateBack = { navController.popBackStack() },

                )
            }
        }

        composable(Screen.Statistics.route) { backStackEntry ->
            val currentUser by authViewModel.currentUser.collectAsState()
            StatisticsScreen(
                userId = currentUser?.id ?: 0L,
                viewModel = statisticsViewModel,
                onNavigateBack = {

                    if (backStackEntry.lifecycle.currentState == androidx.lifecycle.Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                }
            )
        }

    }
}