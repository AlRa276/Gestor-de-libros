package com.marianaalra.booklog.ui.feature.library

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.ui.components.BookListSection
import com.marianaalra.booklog.ui.theme.VistaTheme
import com.marianaalra.booklog.ui.viewmodel.BookViewModel
import com.marianaalra.booklog.utils.copyPdfToInternalStorage
import com.marianaalra.booklog.utils.extractPdfCover

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithDrawer(
    bookViewModel: BookViewModel,           // 👈 NUEVO
    usuarioId: Long,
    onNavigateToReading: (String, String?) -> Unit = { _, _ -> },
    onNavigateToNotes: (String, Long) -> Unit = { _, _ -> },
    onLogout: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("Biblioteca") }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    // --- AQUÍ SIMULAMOS TU BASE DE DATOS (Lista Maestra) ---
    val books by bookViewModel.books.collectAsState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val internalPath = copyPdfToInternalStorage(context, uri)

                if (internalPath != null) {
                    // 👇 Extraemos la portada justo después de copiar
                    val coverPath = extractPdfCover(context, internalPath)

                    val nombreReal = context.contentResolver
                        .query(uri, null, null, null, null)
                        ?.use { cursor ->
                            val nameIndex = cursor.getColumnIndex(
                                android.provider.OpenableColumns.DISPLAY_NAME
                            )
                            cursor.moveToFirst()
                            cursor.getString(nameIndex)
                        } ?: "Nuevo Documento.pdf"

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

                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Book, contentDescription = null) },
                    label = { Text("Biblioteca") },
                    selected = selectedItem == "Biblioteca",
                    onClick = {
                        selectedItem = "Biblioteca"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = null) },
                    label = { Text("Pendientes") },
                    selected = selectedItem == "Pendientes",
                    onClick = {
                        selectedItem = "Pendientes"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.MenuBook, contentDescription = null) },
                    label = { Text("En progreso") },
                    selected = selectedItem == "En progreso",
                    onClick = {
                        selectedItem = "En progreso"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.CheckCircle, contentDescription = null) },
                    label = { Text("Finalizadas") },
                    selected = selectedItem == "Finalizadas",
                    onClick = {
                        selectedItem = "Finalizadas"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.LibraryBooks, contentDescription = null) },
                    label = { Text("Series") },
                    selected = selectedItem == "Series",
                    onClick = {
                        selectedItem = "Series"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    label = { Text("Autores") },
                    selected = selectedItem == "Autores",
                    onClick = {
                        selectedItem = "Autores"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.Label, contentDescription = null) },
                    label = { Text("Etiquetas") },
                    selected = selectedItem == "Etiquetas",
                    onClick = {
                        selectedItem = "Etiquetas"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

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

        val filteredBooks = when (selectedItem) {
            "Biblioteca" -> books
            "Pendientes" -> books.filter { it.status == "PENDIENTE" }
            "En progreso" -> books.filter { it.status == "EN_PROGRESO" }
            "Finalizadas" -> books.filter { it.status == "FINALIZADA" }
            else -> emptyList()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp)
                                .height(52.dp),
                            placeholder = { Text("Buscar...", style = MaterialTheme.typography.bodyMedium) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Icono de búsqueda") },
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
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú lateral")
                        }
                    }
                )
            },
            // 👇 AQUÍ ESTÁ EL BOTÓN FLOTANTE QUE FALTABA
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        filePickerLauncher.launch(arrayOf("application/pdf", "application/epub+zip"))
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar nuevo libro")
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

                Spacer(modifier = Modifier.height(16.dp))

                BookListSection(
                    booksToShow = filteredBooks,
                    onNavigateToReading = onNavigateToReading,
                    onNavigateToNotes = { title ->
                        val book = books.find { it.title == title }
                        onNavigateToNotes(title, book?.id ?: 0L)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainScreenWithDrawerPreview() {
    VistaTheme {
    }
}