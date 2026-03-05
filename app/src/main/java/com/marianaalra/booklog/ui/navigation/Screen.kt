package com.marianaalra.booklog.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    // Rutas dinámicas que reciben el título del libro
    object Reading : Screen("reading/{bookTitle}") {
        fun createRoute(title: String) = "reading/$title"
    }
    object Notes : Screen("notes/{bookTitle}") {
        fun createRoute(title: String) = "notes/$title"
    }
}