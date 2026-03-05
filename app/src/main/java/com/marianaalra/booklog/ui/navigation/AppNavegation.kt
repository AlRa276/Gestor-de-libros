// AppNavegation.kt
package com.marianaalra.booklog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marianaalra.booklog.BookLogApp
import com.marianaalra.booklog.ui.feature.auth.LoginScreen
import com.marianaalra.booklog.ui.feature.auth.RegisterScreen
import com.marianaalra.booklog.ui.feature.library.MainScreenWithDrawer
import com.marianaalra.booklog.ui.feature.notes.NotesAndQuotesScreen
import com.marianaalra.booklog.ui.feature.reading.ReadingScreen
import com.marianaalra.booklog.ui.viewmodel.AuthViewModel
import com.marianaalra.booklog.ui.viewmodel.BookViewModel
import com.marianaalra.booklog.ui.viewmodel.NotesViewModel
import com.marianaalra.booklog.ui.viewmodel.ViewModelFactory
import java.net.URLDecoder

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

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            val error by authViewModel.error.collectAsState()
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

        composable(Screen.Home.route) {
            val currentUser by authViewModel.currentUser.collectAsState()
            if (currentUser == null) {
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
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
            val fileUri = if (encodedUri.isNullOrEmpty()) null else URLDecoder.decode(encodedUri, "UTF-8")
            val bookId = backStackEntry.arguments?.getString("bookId")?.toLongOrNull() ?: 0L

            val books by bookViewModel.books.collectAsState()
            val currentBook = books.find { it.id == bookId }  // 👈 NUEVO

            ReadingScreen(
                bookTitle = title,
                fileUriString = fileUri,
                bookId = bookId,
                currentBook = currentBook,              // 👈 NUEVO
                notesViewModel = notesViewModel,
                bookViewModel = bookViewModel,          // 👈 NUEVO
                initialProgress = currentBook?.progress ?: 0f,  // 👈 Carga el progreso real
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

            // Cargamos los datos de este libro específico
            androidx.compose.runtime.LaunchedEffect(bookId) {
                notesViewModel.loadNotesAndQuotes(bookId)
            }

            NotesAndQuotesScreen(
                bookTitle = title,
                notes = notes,
                quotes = quotes,
                onNavigateBack = { navController.popBackStack() },
                onEditNote = { /* TODO */ },
                onDeleteNote = { notesViewModel.deleteNote(it) },
                onEditQuote = { /* TODO */ },
                onDeleteQuote = { notesViewModel.deleteQuote(it) }
            )
        }
    }
}