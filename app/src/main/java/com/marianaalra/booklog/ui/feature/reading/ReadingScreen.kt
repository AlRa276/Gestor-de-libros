package com.marianaalra.booklog.ui.feature.reading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.NoteAdd
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 👇 IMPORTANTE: Asegúrate de que esta línea coincida con la ubicación real de tu tema
import com.marianaalra.booklog.ui.theme.VistaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    bookTitle: String,
    initialProgress: Float,
    onNavigateBack: () -> Unit,
    onAddNote: () -> Unit, // 👈 Nueva acción para Notas
    onAddCitation: () -> Unit, // 👈 Nueva acción para Citas
    modifier: Modifier = Modifier
) {
    var currentProgress by remember { mutableFloatStateOf(initialProgress) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = bookTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar a la biblioteca")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Guardar marcador rápido */ }) {
                        Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Guardar marcador")
                    }
                    IconButton(onClick = { /* Configuración visual */ }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Configuración de lectura")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // --- NUEVA SECCIÓN: BOTONES DE NOTAS Y CITAS ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onAddCitation) {
                            Icon(Icons.Outlined.FormatQuote, contentDescription = "Crear Cita")
                            Spacer(Modifier.width(8.dp))
                            Text("Citar")
                        }

                        // Un pequeño separador visual entre los botones
                        Text(text = "|", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))

                        TextButton(onClick = onAddNote) {
                            Icon(Icons.Outlined.NoteAdd, contentDescription = "Añadir Nota")
                            Spacer(Modifier.width(8.dp))
                            Text("Anotar")
                        }
                    }

                    // Línea divisoria sutil
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
                    )

                    // --- SECCIÓN: PROGRESO ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Slider(
                            value = currentProgress,
                            onValueChange = { currentProgress = it },
                            valueRange = 0f..1f,
                            modifier = Modifier.weight(1f)
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

            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n\n" +
                        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n\n" +
                        "Aquí es donde eventualmente se renderizará el contenido real de tus archivos. Por ahora, podemos usar este espacio para diseñar cómo queremos que se vean los márgenes, el color de fondo y la tipografía de lectura para que la experiencia sea perfecta.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 28.sp,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.padding(bottom = 24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReadingScreenPreview() {
    // 👇 AQUÍ IMPLEMENTAMOS TU TEMA
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