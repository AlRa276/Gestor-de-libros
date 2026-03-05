package com.marianaalra.booklog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marianaalra.booklog.ui.feature.auth.LoginScreen
import com.marianaalra.booklog.ui.feature.library.MainScreenWithDrawer
import com.marianaalra.booklog.ui.feature.notes.NotesQuotesScreen
import com.marianaalra.booklog.ui.feature.reading.ReadingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // 1. PANTALLA DE LOGIN
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { username, password ->
                    // Al iniciar sesión, vamos al Home y destruimos el historial del login
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        // 2. PANTALLA PRINCIPAL (BIBLIOTECA)
        composable(Screen.Home.route) {
            MainScreenWithDrawer(
                onNavigateToReading = { bookTitle ->
                    navController.navigate(Screen.Reading.createRoute(bookTitle))
                },
                onNavigateToNotes = { bookTitle ->
                    navController.navigate(Screen.Notes.createRoute(bookTitle))
                },
                onLogout = {
                    // Al cerrar sesión, regresamos al login limpiando todo
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        // 3. PANTALLA DE LECTURA
        composable(Screen.Reading.route) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("bookTitle") ?: "Libro"
            ReadingScreen(
                bookTitle = title,
                initialProgress = 0f,
                onNavigateBack = { navController.popBackStack() },
                onAddNote = { /* Acción futura */ },
                onAddCitation = { /* Acción futura */ }
            )
        }

        // 4. PANTALLA DE NOTAS Y CITAS
        composable(Screen.Notes.route) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("bookTitle") ?: "Libro"
            NotesQuotesScreen(
                bookTitle = title,
                notes = emptyList(), // Lista temporal
                quotes = emptyList(), // Lista temporal
                onNavigateBack = { navController.popBackStack() },
                onEditNote = {}, onDeleteNote = {},
                onEditQuote = {}, onDeleteQuote = {}
            )
        }
    }
}