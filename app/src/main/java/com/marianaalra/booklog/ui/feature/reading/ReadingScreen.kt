package com.marianaalra.booklog.ui.feature.reading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.NoteAdd
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marianaalra.booklog.ui.theme.VistaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    bookTitle: String,
    initialProgress: Float,
    onNavigateBack: () -> Unit,
    onAddNote: () -> Unit,
    onAddCitation: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentProgress by remember { mutableFloatStateOf(initialProgress) }

    // --- ESTADOS PARA LOS CUADROS DE DIÁLOGO ---
    var showNoteDialog by remember { mutableStateOf(false) }
    var showQuoteDialog by remember { mutableStateOf(false) }

    // Variables temporales para guardar lo que el usuario escribe
    var noteContent by remember { mutableStateOf("") }
    var quoteText by remember { mutableStateOf("") }
    var quoteComment by remember { mutableStateOf("") }

    // --- DIÁLOGO PARA NOTAS ---
    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text("Nueva Nota") },
            text = {
                OutlinedTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    label = { Text("Escribe tu idea...") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 5
                )
            },
            confirmButton = {
                Button(onClick = {
                    /* TODO: Aquí llamaremos a la Base de Datos para guardar */
                    showNoteDialog = false
                    noteContent = "" // Limpiamos el campo
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // --- DIÁLOGO PARA CITAS ---
    if (showQuoteDialog) {
        AlertDialog(
            onDismissRequest = { showQuoteDialog = false },
            title = { Text("Guardar Cita") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quoteText,
                        onValueChange = { quoteText = it },
                        label = { Text("Texto citado (Copiado del libro)") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 4
                    )
                    OutlinedTextField(
                        value = quoteComment,
                        onValueChange = { quoteComment = it },
                        label = { Text("Comentario u opinión (Opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    /* TODO: Aquí llamaremos a la Base de Datos para guardar */
                    showQuoteDialog = false
                    quoteText = ""
                    quoteComment = ""
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showQuoteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = bookTitle, maxLines = 1, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { }) { Icon(Icons.Outlined.BookmarkBorder, "Marcador") }
                    IconButton(onClick = { }) { Icon(Icons.Outlined.Settings, "Configuración") }
                }
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, tonalElevation = 3.dp) {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                    // BOTONES PARA ABRIR DIÁLOGOS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showQuoteDialog = true }) { // 👈 Abre ventana Cita
                            Icon(Icons.Outlined.FormatQuote, contentDescription = "Crear Cita")
                            Spacer(Modifier.width(8.dp))
                            Text("Citar")
                        }
                        Text(text = "|", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                        TextButton(onClick = { showNoteDialog = true }) { // 👈 Abre ventana Nota
                            Icon(Icons.Outlined.NoteAdd, contentDescription = "Añadir Nota")
                            Spacer(Modifier.width(8.dp))
                            Text("Anotar")
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Slider(
                            value = currentProgress, onValueChange = { currentProgress = it },
                            valueRange = 0f..1f, modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = "${(currentProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        // --- ÁREA DE LECTURA ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Capítulo 1",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )

            // 👇 SELECTION CONTAINER: Permite mantener presionado y copiar el texto
            SelectionContainer {
                Text(
                    text = "En el diseño de interfaces moderno, la cohesión y el acoplamiento son principios fundamentales. \n\n" +
                            "Para hacer una prueba de copiado, mantén presionado este texto. Te aparecerán los selectores nativos de Android. Copia el texto, luego presiona el botón 'Citar' en la barra inferior y pégalo en el recuadro de texto citado.\n\n" +
                            "En una implementación futura con WebView o PdfRenderer, el texto seleccionado se capturaría automáticamente mediante JavaScript o la API del PDF.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 28.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.padding(bottom = 24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReadingScreenPreview() {
    VistaTheme {
        ReadingScreen(
            bookTitle = "Diseño de Interfaces de Usuario",
            initialProgress = 0.35f,
            onNavigateBack = {},
            onAddNote = {},
            onAddCitation = {}
        )
    }
}