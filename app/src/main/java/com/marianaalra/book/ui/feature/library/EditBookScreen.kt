package com.marianaalra.book.ui.feature.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marianaalra.book.domain.model.Book
import com.marianaalra.book.domain.model.ColeccionDomain
import com.marianaalra.book.domain.model.SerieDomain
import com.marianaalra.book.ui.viewmodel.EditBookViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookScreen(
    book: Book,
    userId: Long,
    editBookViewModel: EditBookViewModel,
    onNavigateBack: () -> Unit
) {
    // Campos editables
    var titulo by remember { mutableStateOf(book.title) }
    var autor by remember { mutableStateOf(book.author ?: "") }
    var serieInput by remember { mutableStateOf("") }
    var serieSeleccionada by remember { mutableStateOf<SerieDomain?>(
        book.serieId?.let { id -> SerieDomain(id = id, nombre = "") }
    )}
    var coleccionInput by remember { mutableStateOf("") }

    val series by editBookViewModel.series.collectAsState()
    val colecciones by editBookViewModel.colecciones.collectAsState()
    val coleccionesDelLibro by editBookViewModel.coleccionesDelLibro.collectAsState()
    val saved by editBookViewModel.saved.collectAsState()
    val error by editBookViewModel.error.collectAsState()

    // Colecciones seleccionadas para este libro
    var selectedColecciones by remember { mutableStateOf<List<ColeccionDomain>>(emptyList()) }

    // Sugerencias de autocomplete
    val seriesFiltradas = series.filter {
        serieInput.isNotBlank() && it.nombre.contains(serieInput, ignoreCase = true)
    }
    val coleccionesFiltradas = colecciones.filter {
        coleccionInput.isNotBlank() &&
                it.nombre.contains(coleccionInput, ignoreCase = true) &&
                selectedColecciones.none { sel -> sel.id == it.id }
    }

    // Cargamos datos y sincronizamos colecciones del libro
    LaunchedEffect(Unit) {
        editBookViewModel.loadData(userId, book.id)
    }
    LaunchedEffect(coleccionesDelLibro) {
        if (selectedColecciones.isEmpty() && coleccionesDelLibro.isNotEmpty()) {
            selectedColecciones = coleccionesDelLibro
        }
    }
    // Sincronizamos el nombre de la serie seleccionada
    LaunchedEffect(series) {
        if (serieSeleccionada != null && serieInput.isBlank()) {
            val serieReal = series.find { it.id == serieSeleccionada?.id }
            if (serieReal != null) serieInput = serieReal.nombre
        }
    }

    // Navegamos atrás cuando se guarda
    LaunchedEffect(saved) {
        if (saved) {
            editBookViewModel.clearSaved()
            onNavigateBack()
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar ficha técnica") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // --- TÍTULO ---
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --- AUTOR ---
            OutlinedTextField(
                value = autor,
                onValueChange = { autor = it },
                label = { Text("Autor (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --- SERIE con autocomplete ---
            Text("Serie (opcional)", style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            OutlinedTextField(
                value = serieInput,
                onValueChange = {
                    serieInput = it
                    if (it.isBlank()) serieSeleccionada = null
                },
                label = { Text("Buscar o crear serie") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (serieSeleccionada != null) {
                        IconButton(onClick = {
                            serieSeleccionada = null
                            serieInput = ""
                        }) {
                            Icon(Icons.Default.Close, "Quitar serie")
                        }
                    }
                }
            )

            // Sugerencias de series
            if (seriesFiltradas.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        seriesFiltradas.forEach { serie ->
                            TextButton(
                                onClick = {
                                    serieSeleccionada = serie
                                    serieInput = serie.nombre
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(serie.nombre, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }

            // Opción de crear serie nueva
            if (serieInput.isNotBlank() && serieSeleccionada == null &&
                series.none { it.nombre.equals(serieInput, ignoreCase = true) }
            ) {
                OutlinedButton(
                    onClick = {
                        GlobalScope.launch {
                            val nueva = editBookViewModel.getOrCreateSerie(serieInput, userId)
                            serieSeleccionada = nueva
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Crear serie \"$serieInput\"")
                }
            }

            // --- COLECCIONES / ETIQUETAS ---
            Text("Etiquetas / Colecciones", style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Chips de colecciones seleccionadas
            if (selectedColecciones.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(selectedColecciones) { col ->
                        InputChip(
                            selected = true,
                            onClick = { },
                            label = { Text(col.nombre) },
                            trailingIcon = {
                                IconButton(
                                    onClick = { selectedColecciones = selectedColecciones - col },
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Icon(Icons.Default.Close, "Quitar", modifier = Modifier.size(14.dp))
                                }
                            }
                        )
                    }
                }
            }

            // Campo para agregar colección
            OutlinedTextField(
                value = coleccionInput,
                onValueChange = { coleccionInput = it },
                label = { Text("Buscar o crear etiqueta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Sugerencias de colecciones
            if (coleccionesFiltradas.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        coleccionesFiltradas.forEach { col ->
                            TextButton(
                                onClick = {
                                    selectedColecciones = selectedColecciones + col
                                    coleccionInput = ""
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(col.nombre, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }

            // Opción de crear colección nueva
            if (coleccionInput.isNotBlank() &&
                colecciones.none { it.nombre.equals(coleccionInput, ignoreCase = true) }
            ) {
                OutlinedButton(
                    onClick = {
                        GlobalScope.launch {
                            val nueva = editBookViewModel.getOrCreateColeccion(coleccionInput, userId)
                            selectedColecciones = selectedColecciones + nueva
                            coleccionInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Crear etiqueta \"$coleccionInput\"")
                }
            }

            // Error
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            // --- BOTÓN GUARDAR ---
            Button(
                onClick = {
                    editBookViewModel.saveBook(
                        book.copy(
                            title = titulo.trim(),
                            author = autor.trim().ifBlank { null },
                            serieId = serieSeleccionada?.id
                        ),
                        selectedColecciones
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = titulo.isNotBlank()
            ) {
                Text("Guardar cambios")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}