package com.marianaalra.booklog.ui.feature.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 👇 Importamos los modelos y las cards que acabamos de separar
import com.marianaalra.booklog.domain.model.NoteDomain
import com.marianaalra.booklog.domain.model.QuoteDomain
import com.marianaalra.booklog.ui.components.NoteCard
import com.marianaalra.booklog.ui.components.QuoteCard
import com.marianaalra.booklog.ui.theme.VistaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesAndQuotesScreen(
    bookTitle: String,
    notes: List<NoteDomain>,
    quotes: List<QuoteDomain>,
    onNavigateBack: () -> Unit,
    onEditNote: (NoteDomain) -> Unit,
    onDeleteNote: (NoteDomain) -> Unit,
    onEditQuote: (QuoteDomain) -> Unit,
    onDeleteQuote: (QuoteDomain) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Notas", "Citas")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Anotaciones: $bookTitle",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                        }
                    }
                )
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTabIndex) {
                0 -> NotesList(notes, onEditNote, onDeleteNote)
                1 -> QuotesList(quotes, onEditQuote, onDeleteQuote)
            }
        }
    }
}

@Composable
fun NotesList(notes: List<NoteDomain>, onEdit: (NoteDomain) -> Unit, onDelete: (NoteDomain) -> Unit) {
    if (notes.isEmpty()) {
        EmptyStateMessage("Aún no tienes notas para este libro.")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(notes) { note ->
                // Usamos la NoteCard que está en la carpeta components
                NoteCard(note = note, onEdit = { onEdit(note) }, onDelete = { onDelete(note) })
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun QuotesList(quotes: List<QuoteDomain>, onEdit: (QuoteDomain) -> Unit, onDelete: (QuoteDomain) -> Unit) {
    if (quotes.isEmpty()) {
        EmptyStateMessage("Aún no has guardado citas de este libro.")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(quotes) { quote ->
                // Usamos la QuoteCard que está en la carpeta components
                QuoteCard(quote = quote, onEdit = { onEdit(quote) }, onDelete = { onDelete(quote) })
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun EmptyStateMessage(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotesAndQuotesScreenPreview() {
    val dummyNotes = listOf(
        NoteDomain(1, "Repasar el concepto de cohesión y acoplamiento para el examen.", "45", "04 Mar 2026"),
        NoteDomain(2, "El autor menciona que los diagramas UML son opcionales pero recomendados.", "112", "04 Mar 2026")
    )
    val dummyQuotes = listOf(
        QuoteDomain(1, "La simplicidad es el alma de la eficiencia.", "Me encantó esta frase para la introducción de mi ensayo.", "22", "04 Mar 2026"),
        QuoteDomain(2, "Cualquier tonto puede escribir código que un ordenador entienda. Los buenos programadores escriben código que los humanos puedan entender.", null, "89", "04 Mar 2026")
    )

    VistaTheme {
        NotesAndQuotesScreen(
            bookTitle = "Diseño de Interfaces",
            notes = dummyNotes,
            quotes = dummyQuotes,
            onNavigateBack = {},
            onEditNote = {},
            onDeleteNote = {},
            onEditQuote = {},
            onDeleteQuote = {}
        )
    }
}