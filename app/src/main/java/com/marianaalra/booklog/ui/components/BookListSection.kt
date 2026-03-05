package com.marianaalra.booklog.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.marianaalra.booklog.domain.model.Book

@Composable
fun BookListSection(
    booksToShow: List<Book>, // 👈 Recibe la lista dinámica
    onNavigateToReading: (String, String?) -> Unit = { _, _ -> },
    onNavigateToNotes: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(booksToShow) { book ->
            BookCard(
                title = book.title,
                fileFormat = book.fileFormat,
                progress = book.progress,
                status = book.status,
                // Asignamos un color por defecto o podrías agregarlo a tu data class
                coverColor = Color(0xFFBCAAA4),
                onOpenReading = {
                    // Mandamos el título Y la ruta guardada en el objeto libro
                    onNavigateToReading(book.title, book.fileUri)
                },
                onNotesClick = { onNavigateToNotes(book.title) },
                onEditClick = { },
                onStatusChange = { }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}