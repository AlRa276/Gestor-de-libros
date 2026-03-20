package com.marianaalra.booklog.ui.feature.library

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onLogout: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Estados de búsqueda y navegación
    var selectedItem by remember { mutableStateOf("Biblioteca") }
    var searchQuery by remember { mutableStateOf("") }

    // --- CATEGORÍAS ESTÁTICAS ---
    // Usamos una lista mutable en memoria para que el botón "Agregar" funcione durante la sesión
    val categories = remember { mutableStateListOf("Todos", "Ficción", "No Ficción", "Misterio") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

    val books by bookViewModel.books.collectAsState()

    LaunchedEffect(usuarioId) {
        if (usuarioId != 0L) {
            bookViewModel.loadBooks(usuarioId)
        }
    }

    // Lógica de filtrado (Status del Drawer + Buscador)
    // Nota: Como 'Book' no tiene 'genre', el filtro de categoría arriba solo sirve para navegación visual por ahora
    val filteredBooks = books.filter { book ->
        val matchesDrawer = when (selectedItem) {
            "Biblioteca" -> true
            "Pendientes" -> book.status == "PENDIENTE"
            "En progreso" -> book.status == "EN_PROGRESO"
            "Finalizadas" -> book.status == "FINALIZADA"
            else -> true
        }
        val matchesSearch = book.title.contains(searchQuery, ignoreCase = true)
        matchesDrawer && matchesSearch
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val internalPath = copyPdfToInternalStorage(context, uri)
                if (internalPath != null) {
                    val coverPath = extractPdfCover(context, internalPath)
                    val nombreReal = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        cursor.getString(nameIndex)
                    } ?: "Nuevo Libro.pdf"

                    val titulo = nombreReal.substringBeforeLast(".")

                    bookViewModel.addBook(
                        Book(
                            usuarioId = usuarioId,
                            title = titulo,
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
                Text(
                    text = "BookLog",
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp))

                val menuItems = listOf(
                    "Biblioteca" to Icons.Outlined.Book,
                    "Pendientes" to Icons.Outlined.BookmarkBorder,
                    "En progreso" to Icons.Outlined.MenuBook,
                    "Finalizadas" to Icons.Outlined.CheckCircle
                )

                menuItems.forEach { (label, icon) ->
                    NavigationDrawerItem(
                        icon = { Icon(icon, contentDescription = null) },
                        label = { Text(label) },
                        selected = selectedItem == label,
                        onClick = {
                            selectedItem = label
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Logout, contentDescription = null) },
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth().padding(end = 16.dp).height(52.dp),
                            placeholder = { Text("Buscar...", style = MaterialTheme.typography.bodyMedium) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            shape = RoundedCornerShape(50),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { filePickerLauncher.launch(arrayOf("application/pdf", "application/epub+zip")) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = selectedItem,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // --- APARTADO DE CATEGORÍAS CON BOTÓN AGREGAR ---
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            leadingIcon = if (selectedCategory == category) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                    item {
                        // Botón Estático para agregar otra categoría
                        InputChip(
                            selected = false,
                            onClick = { showAddCategoryDialog = true },
                            label = { Text("Nueva") },
                            trailingIcon = { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                BookListSection(
                    booksToShow = filteredBooks,
                    onNavigateToReading = onNavigateToReading,
                    onNavigateToNotes = { title ->
                        val book = books.find { it.title == title }
                        onNavigateToNotes(title, book?.id ?: 0L)
                    },
                    onStatusChange = { book, newStatus ->
                        bookViewModel.updateBook(book.copy(status = newStatus))
                    }
                )
            }
        }

        // --- DIÁLOGO PARA AGREGAR CATEGORÍA ---
        if (showAddCategoryDialog) {
            AlertDialog(
                onDismissRequest = { showAddCategoryDialog = false },
                title = { Text("Añadir Categoría") },
                text = {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Nombre") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newCategoryName.isNotBlank()) {
                            categories.add(newCategoryName)
                            newCategoryName = ""
                            showAddCategoryDialog = false
                        }
                    }) {
                        Text("Añadir")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddCategoryDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}