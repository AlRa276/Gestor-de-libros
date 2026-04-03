package com.marianaalra.booklog.ui.feature.library

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.ui.components.BookListSection
import com.marianaalra.booklog.ui.viewmodel.BookViewModel
import com.marianaalra.booklog.utils.copyPdfToInternalStorage
import com.marianaalra.booklog.utils.extractPdfCover
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithDrawer(
    bookViewModel: BookViewModel,
    usuarioId: Long,
    onNavigateToReading: (String, String?) -> Unit = { _, _ -> },
    onNavigateToNotes: (String, Long) -> Unit = { _, _ -> },
    onNavigateToEdit: (Long) -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val selectedItem by bookViewModel.selectedSection.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Estados para subniveles de navegación
    val selectedAuthor by bookViewModel.selectedAuthor.collectAsState()
    val selectedSerieId by bookViewModel.selectedSerieId.collectAsState()
    val selectedColeccionId by bookViewModel.selectedColeccionId.collectAsState()

    val books by bookViewModel.books.collectAsState()
    val series by bookViewModel.series.collectAsState()
    val colecciones by bookViewModel.colecciones.collectAsState()

    // Filtrado reactivo por etiqueta
    val booksByColeccion by if (selectedItem == "Etiquetas" && selectedColeccionId != null) {
        bookViewModel.getBooksByColeccion(selectedColeccionId!!).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList()) }
    }

    LaunchedEffect(usuarioId) {
        if (usuarioId != 0L) {
            bookViewModel.loadBooks(usuarioId)
            bookViewModel.loadSeries(usuarioId)
            bookViewModel.loadColecciones(usuarioId)
        }
    }

    LaunchedEffect(selectedItem) {
        searchQuery = ""
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val internalPath = copyPdfToInternalStorage(context, uri)
                if (internalPath != null) {
                    val coverPath = extractPdfCover(context, internalPath)
                    val nombreReal = context.contentResolver
                        .query(uri, null, null, null, null)
                        ?.use { cursor ->
                            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                            cursor.moveToFirst()
                            cursor.getString(nameIndex)
                        } ?: "Libro.pdf"

                    bookViewModel.addBook(
                        Book(
                            usuarioId = usuarioId,
                            title = nombreReal.substringBeforeLast("."),
                            fileFormat = "pdf",
                            fileUri = internalPath,
                            nombreArchivo = nombreReal,
                            coverPath = coverPath
                        )
                    )
                }
            }
        }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text("BookLog", modifier = Modifier.padding(28.dp), style = MaterialTheme.typography.titleLarge)
                HorizontalDivider()

                val menuOptions = listOf(
                    "Biblioteca" to Icons.Outlined.Book,
                    "Pendientes" to Icons.Outlined.BookmarkBorder,
                    "En progreso" to Icons.Outlined.MenuBook,
                    "Finalizadas" to Icons.Outlined.CheckCircle,
                    "Series" to Icons.Outlined.LibraryBooks,
                    "Autores" to Icons.Outlined.Person,
                    "Etiquetas" to Icons.Outlined.Label,
                    "Estadísticas" to Icons.Outlined.BarChart
                )

                menuOptions.forEach { (label, icon) ->
                    NavigationDrawerItem(
                        icon = { Icon(icon, null) },
                        label = { Text(label) },
                        selected = selectedItem == label,
                        onClick = {
                            if (label == "Estadísticas") {
                                onNavigateToStatistics()
                            } else {
                                bookViewModel.setSelectedSection(label)
                            }
                            scope.launch { drawerState.close() }
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Logout, null) },
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onLogout() }
                )
            }
        }
    ) {
        // Lógica de filtrado de libros según la sección activa
        val filteredBooks = when (selectedItem) {
            "Biblioteca" -> books
            "Pendientes" -> books.filter { it.status == "PENDIENTE" }
            "En progreso" -> books.filter { it.status == "EN_PROGRESO" }
            "Finalizadas" -> books.filter { it.status == "FINALIZADA" }
            "Autores" -> if (selectedAuthor != null) books.filter { it.author == selectedAuthor } else emptyList()
            "Series" -> if (selectedSerieId != null) books.filter { it.serieId == selectedSerieId } else emptyList()
            "Etiquetas" -> booksByColeccion
            else -> emptyList()
        }.filter { it.title.contains(searchQuery, true) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                            placeholder = { Text("Buscar...") },
                            leadingIcon = { Icon(Icons.Default.Search, null) },
                            shape = RoundedCornerShape(50),
                            singleLine = true
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            when {
                                selectedAuthor != null -> bookViewModel.setSelectedAuthor(null)
                                selectedSerieId != null -> bookViewModel.setSelectedSerieId(null)
                                selectedColeccionId != null -> bookViewModel.setSelectedColeccionId(null)
                                else -> scope.launch { drawerState.open() }
                            }
                        }) {
                            Icon(
                                if (selectedAuthor != null || selectedSerieId != null || selectedColeccionId != null)
                                    Icons.Default.ArrowBack else Icons.Default.Menu, null
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { filePickerLauncher.launch(arrayOf("application/pdf")) }) {
                    Icon(Icons.Default.Add, null)
                }
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {

                val currentTitle = when {
                    selectedAuthor != null -> "Autor: $selectedAuthor"
                    selectedSerieId != null -> "Serie: ${series.find { it.id == selectedSerieId }?.nombre ?: ""}"
                    selectedColeccionId != null -> "Etiqueta: ${colecciones.find { it.id == selectedColeccionId }?.nombre ?: ""}"
                    else -> selectedItem
                }
                Text(currentTitle, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(16.dp))

                when {
                    // SECCIÓN AUTORES
                    selectedItem == "Autores" && selectedAuthor == null -> {
                        val authors = books.mapNotNull { it.author }.distinct().filter { it.isNotBlank() }
                        LazyColumn {
                            items(authors) { author ->
                                ListItem(
                                    headlineContent = { Text(author) },
                                    leadingContent = { Icon(Icons.Default.Person, null) },
                                    modifier = Modifier.clickable { bookViewModel.setSelectedAuthor(author) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }

                    // SECCIÓN SERIES
                    selectedItem == "Series" && selectedSerieId == null -> {
                        if (series.isEmpty()) Text("No hay series creadas.")
                        LazyColumn {
                            items(series) { serie ->
                                ListItem(
                                    headlineContent = { Text(serie.nombre) },
                                    leadingContent = { Icon(Icons.Default.LibraryBooks, null) },
                                    modifier = Modifier.clickable { bookViewModel.setSelectedSerieId(serie.id) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }

                    // SECCIÓN ETIQUETAS
                    selectedItem == "Etiquetas" && selectedColeccionId == null -> {
                        if (colecciones.isEmpty()) Text("No hay etiquetas creadas.")
                        LazyColumn {
                            items(colecciones) { col ->
                                ListItem(
                                    headlineContent = { Text(col.nombre) },
                                    leadingContent = { Icon(Icons.Default.Label, null) },
                                    modifier = Modifier.clickable { bookViewModel.setSelectedColeccionId(col.id) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }

                    // VISTA DE LIBROS
                    else -> {
                        BookListSection(
                            booksToShow = filteredBooks,
                            onNavigateToReading = onNavigateToReading,
                            onNavigateToNotes = { title ->
                                val book = books.find { it.title == title }
                                onNavigateToNotes(title, book?.id ?: 0)
                            },
                            onStatusChange = { book, status ->
                                bookViewModel.updateBook(book.copy(status = status))
                            },
                            onEditClick = { onNavigateToEdit(it.id) }
                        )
                    }
                }
            }
        }
    }
}