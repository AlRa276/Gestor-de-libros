package com.marianaalra.booklog.ui.feature.library

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// 👇 Asegúrate de que estas dos líneas apunten correctamente a tus archivos
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.ui.components.BookListSection
import com.marianaalra.booklog.ui.theme.VistaTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithDrawer(
    onNavigateToReading: (String, String?) -> Unit = { _, _ -> },
    onNavigateToNotes: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {

// 1. Primero defines los estados del Drawer y Corrutinas
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("Biblioteca") }
    var searchQuery by remember { mutableStateOf("") }

    // --- AQUÍ SIMULAMOS TU BASE DE DATOS (Lista Maestra) ---
    val allBooks = remember {
        mutableStateListOf(
            Book("Diseño de Interfaces de Usuario", "pdf", 0.45f, "EN_PROGRESO"),
            Book("Estructuras de Datos y Algoritmos", "epub", 0.0f, "PENDIENTE"),
            Book("Análisis de Sistemas Complejos", "pdf", 1.0f, "FINALIZADA"),
            Book("Cálculo Multivariable", "pdf", 0.0f, "PENDIENTE"),
            Book("Metodologías de Desarrollo de Software", "epub", 0.80f, "EN_PROGRESO")
        )
    }
    // 2. Aquí es donde va el código que mencionas (antes del Scaffold)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val isEpub = uri.toString().contains("epub", ignoreCase = true)
                val format = if (isEpub) "epub" else "pdf"
                val tempName = uri.path?.substringAfterLast("/")?.substringBeforeLast(".") ?: "Nuevo Documento"

                allBooks.add(
                    Book(
                        title = tempName,
                        fileFormat = format,
                        progress = 0.0f,
                        status = "PENDIENTE",
                        fileUri = uri.toString()
                    )
                )
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
                    modifier = Modifier
                        .padding(horizontal = 28
                            .dp, vertical = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp))

                // --- SECCIÓN: ESTADOS DE LECTURA ---
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

                // --- SECCIÓN: ORGANIZACIÓN ---
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

                // --- SECCIÓN: CUENTA ---
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


        // --- AQUÍ APLICAMOS LA MAGIA DEL FILTRO ---
        val filteredBooks = when (selectedItem) {
            "Biblioteca" -> allBooks // Muestra todos
            "Pendientes" -> allBooks.filter { it.status == "PENDIENTE" }
            "En progreso" -> allBooks.filter { it.status == "EN_PROGRESO" }
            "Finalizadas" -> allBooks.filter { it.status == "FINALIZADA" }
            else -> emptyList() // Para autores, etiquetas, etc., por ahora vacío
        }

        // --- CONTENIDO PRINCIPAL ---
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

                //AQUÍ LLAMAS A TU VISTA GENÉRICA Y LE PASAS LA LISTA FILTRADA
                BookListSection(
                    booksToShow = filteredBooks,
                    onNavigateToReading = onNavigateToReading,
                    onNavigateToNotes = onNavigateToNotes,
                           )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainScreenWithDrawerPreview() {
    VistaTheme{
        MainScreenWithDrawer()
    }

}