package com.marianaalra.booklog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marianaalra.booklog.ui.feature.auth.LoginScreen
import com.marianaalra.booklog.ui.feature.auth.RegisterScreen
import com.marianaalra.booklog.ui.feature.library.MainScreenWithDrawer
import com.marianaalra.booklog.ui.feature.notes.NotesAndQuotesScreen
import com.marianaalra.booklog.ui.feature.reading.ReadingScreen
import java.net.URLDecoder

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
        // 2. PANTALLA DE REGISTRO
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = { username, email, password ->
                    // Simulamos que al registrarse entra directo a la app
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    // Esto hace que el botón de "Ya tengo cuenta" te regrese al Login
                    navController.popBackStack()
                }
            )
        }
        // 3. PANTALLA PRINCIPAL (BIBLIOTECA)
        composable(Screen.Home.route) {
            MainScreenWithDrawer(
                // 👇 Ahora recibimos tanto el título como la ruta del archivo
                onNavigateToReading = { bookTitle, fileUri ->
                    navController.navigate(Screen.Reading.createRoute(bookTitle, fileUri))
                },
                onNavigateToNotes = { bookTitle ->
                    navController.navigate(Screen.Notes.createRoute(bookTitle))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }


        // --- 4. PANTALLA DE LECTURA ---
        composable(Screen.Reading.route) { backStackEntry ->
            // Extraemos los textos protegidos y los regresamos a la normalidad
            val rawTitle = backStackEntry.arguments?.getString("bookTitle") ?: "Libro"
            val title = URLDecoder.decode(rawTitle, "UTF-8")

            val encodedUri = backStackEntry.arguments?.getString("fileUri")
            val fileUri = if (encodedUri.isNullOrEmpty()) null else URLDecoder.decode(encodedUri, "UTF-8")

            ReadingScreen(
                bookTitle = title,
                fileUriString = fileUri, // ¡Ahora sí recibe la ruta intacta!
                initialProgress = 0f,
                onNavigateBack = { navController.popBackStack() },
                onAddNote = { },
                onAddCitation = { }
            )
        }

        // 4. PANTALLA DE NOTAS Y CITAS
        composable(Screen.Notes.route) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("bookTitle") ?: "Libro"
            NotesAndQuotesScreen(
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