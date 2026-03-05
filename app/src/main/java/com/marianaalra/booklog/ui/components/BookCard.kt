package com.marianaalra.booklog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marianaalra.booklog.ui.theme.VistaTheme

@Composable
fun BookCard(
    title: String,
    fileFormat: String,
    progress: Float,
    status: String,
    onOpenReading = { onNavigateToReading(book.title) }, // 👈 Conectado!
    onNotesClick = { onNavigateToNotes(book.title) },
    onOpenReading: () -> Unit,
    onNotesClick: () -> Unit,
    onEditClick: () -> Unit,
    onStatusChange: (newStatus: String) -> Unit,
    modifier: Modifier = Modifier,
    coverColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    var statusMenuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth()
            .height(195.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(120.dp)
                    .aspectRatio(0.72f)
                    .background(Color.Gray, RoundedCornerShape(10.dp))
                    .clickable(onClick = onOpenReading),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.MenuBook,
                    contentDescription = "Abrir lectura",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                }
                Row(

                ) {
                    Text(
                        text = fileFormat.uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = status,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                val safeProgress = progress.coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { safeProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    text = "${(safeProgress * 100).toInt()}% leído",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                //Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNotesClick) {
                        Icon(
                            imageVector = Icons.Outlined.Notes,
                            contentDescription = "Ver notas y citas"
                        )
                    }
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar ficha técnica"
                        )
                    }

                    Box {
                        IconButton(onClick = { statusMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Outlined.SyncAlt,
                                contentDescription = "Cambiar estado de lectura"
                            )
                        }

                        DropdownMenu(
                            expanded = statusMenuExpanded,
                            onDismissRequest = { statusMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("PENDIENTE") },
                                onClick = {
                                    statusMenuExpanded = false
                                    onStatusChange("PENDIENTE")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("EN_PROGRESO") },
                                onClick = {
                                    statusMenuExpanded = false
                                    onStatusChange("EN_PROGRESO")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("FINALIZADA") },
                                onClick = {
                                    statusMenuExpanded = false
                                    onStatusChange("FINALIZADA")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookCardPreview() {
    VistaTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BookCard(
                title = "Introducción a la investigación académica",
                fileFormat = "pdf",
                progress = 0.35f,
                status = "EN_PROGRESO",
                onOpenReading = {},
                onNotesClick = {},
                onEditClick = {},
                onStatusChange = {}
            )
            BookCard(
                title = "Libro pendiente",
                fileFormat = "epub",
                progress = 0f,
                status = "PENDIENTE",
                onOpenReading = {},
                onNotesClick = {},
                onEditClick = {},
                onStatusChange = {}
            )
        }
    }
}

