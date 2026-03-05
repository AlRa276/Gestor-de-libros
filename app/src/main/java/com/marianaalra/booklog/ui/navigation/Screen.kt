package com.marianaalra.booklog.ui.navigation

import java.net.URLEncoder

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    // Rutas dinámicas que reciben el título del libro
    object Reading : Screen("reading/{bookTitle}/{bookId}?fileUri={fileUri}") {
        fun createRoute(title: String, bookId: Long, uri: String?): String {
            val safeTitle = URLEncoder.encode(title, "UTF-8")
            val safeUri = uri?.let { URLEncoder.encode(it, "UTF-8") } ?: ""
            return "reading/${safeTitle}/$bookId?fileUri=$safeUri"
        }
    }
    object Notes : Screen("notes/{bookTitle}/{bookId}") {
        fun createRoute(title: String, bookId: Long) =
            "notes/${URLEncoder.encode(title, "UTF-8")}/$bookId"
    }
}