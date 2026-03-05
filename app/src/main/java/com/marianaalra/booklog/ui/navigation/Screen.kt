package com.marianaalra.booklog.ui.navigation

import java.net.URLEncoder

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    // Rutas dinámicas que reciben el título del libro
    object Reading : Screen("reading/{bookTitle}?fileUri={fileUri}") {
        fun createRoute(title: String, uri: String?): String {
            // Esto transforma los espacios en símbolos seguros (Ej: "Mi Libro" -> "Mi+Libro")
            val safeTitle = URLEncoder.encode(title, "UTF-8")
            val safeUri = uri?.let { URLEncoder.encode(it, "UTF-8") } ?: ""
            return "reading/$safeTitle?fileUri=$safeUri"
        }
    }
    object Notes : Screen("notes/{bookTitle}") {
        fun createRoute(title: String) = "notes/$title"
    }
}