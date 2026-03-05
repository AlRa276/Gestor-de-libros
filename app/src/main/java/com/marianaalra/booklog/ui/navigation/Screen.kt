package com.marianaalra.booklog.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    // Rutas dinámicas que reciben el título del libro
    object Reading : Screen("reading/{bookTitle}?fileUri={fileUri}") {
        fun createRoute(title: String, uri: String?): String {
            // Codificamos la URI para que los símbolos "/" del archivo no rompan la URL
            val encodedUri = uri?.let { android.net.Uri.encode(it) } ?: ""
            return "reading/$title?fileUri=$encodedUri"
        }
    }
    object Notes : Screen("notes/{bookTitle}") {
        fun createRoute(title: String) = "notes/$title"
    }
}