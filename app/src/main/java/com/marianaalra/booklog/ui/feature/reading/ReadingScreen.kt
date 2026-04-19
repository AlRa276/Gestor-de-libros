package com.marianaalra.booklog.ui.feature.reading

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import java.io.File
import androidx.activity.compose.BackHandler
import android.os.ParcelFileDescriptor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.NoteAdd
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.marianaalra.booklog.ui.viewmodel.BookViewModel
import com.marianaalra.booklog.ui.viewmodel.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    bookTitle: String,
    fileUriString: String?,
    bookId: Long = 0L,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToNotes: (String, Long) -> Unit,
    onBack: () -> Unit,
    currentBook: com.marianaalra.booklog.domain.model.Book? = null,  // 👈
    notesViewModel: NotesViewModel? = null,
    bookViewModel: BookViewModel? = null,                             // 👈
    initialProgress: Float,
    onNavigateBack: () -> Unit,
    onAddNote: () -> Unit,
    onAddCitation: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentProgress by remember { mutableFloatStateOf(initialProgress) }
    var sliderProgress by remember { mutableFloatStateOf(initialProgress) }

    LaunchedEffect(sliderProgress) {
        // Guardamos el progreso automáticamente cuando cambia por scroll
        // con un pequeño delay para no saturar la BD
        kotlinx.coroutines.delay(500)
        currentBook?.let { book ->
            if (sliderProgress != book.progress) {
                bookViewModel?.updateBook(book.copy(progress = sliderProgress))
            }
        }
    }
    // Justo después de donde declaras sliderProgress, agrega:
    var totalPages by remember { mutableIntStateOf(0) }
    var initialPageToScroll by remember { mutableIntStateOf(0) }

// Cuando sabemos el total de páginas, calculamos la página inicial
    LaunchedEffect(totalPages) {
        if (totalPages > 0 && initialPageToScroll == 0) {
            initialPageToScroll = (initialProgress * totalPages).toInt().coerceIn(0, totalPages - 1)
        }
    }
    var isNavigatingBack by remember { mutableStateOf(false) }
    LaunchedEffect(currentBook?.progress) {
        currentBook?.progress?.let { savedProgress ->
            sliderProgress = savedProgress
        }
    }
    // Estados para los diálogos
    var showNoteDialog by remember { mutableStateOf(false) }
    var showQuoteDialog by remember { mutableStateOf(false) }
    var noteContent by remember { mutableStateOf("") }
    var quoteText by remember { mutableStateOf("") }
    var quoteComment by remember { mutableStateOf("") }


    // --- DIÁLOGOS ---
    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text("Nueva Nota") },
            text = {
                OutlinedTextField(
                    value = noteContent, onValueChange = { noteContent = it },
                    label = { Text("Escribe tu idea...") },
                    modifier = Modifier.fillMaxWidth().height(120.dp), maxLines = 5
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (noteContent.isNotBlank()) {
                        notesViewModel?.addNote(
                            com.marianaalra.booklog.domain.model.NoteDomain(
                                id = 0,
                                bookId = bookId,
                                contenido = noteContent,
                                referenciaPagina = null
                            )
                        )
                    }
                    showNoteDialog = false
                    noteContent = ""
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if (showQuoteDialog) {
        AlertDialog(
            onDismissRequest = { showQuoteDialog = false },
            title = { Text("Guardar Cita") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quoteText, onValueChange = { quoteText = it },
                        label = { Text("Texto citado del libro") },
                        modifier = Modifier.fillMaxWidth().height(100.dp), maxLines = 4
                    )
                    OutlinedTextField(
                        value = quoteComment, onValueChange = { quoteComment = it },
                        label = { Text("Comentario (Opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (quoteText.isNotBlank()) {
                        notesViewModel?.addQuote(
                            com.marianaalra.booklog.domain.model.QuoteDomain(
                                id = 0,
                                bookId = bookId,
                                textoCitado = quoteText,
                                comentario = quoteComment.ifBlank { null },
                                referenciaPagina = null
                            )
                        )
                    }
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

    BackHandler {
        if (!isNavigatingBack) {
            isNavigatingBack = true
            currentBook?.let { book ->
                bookViewModel?.updateBook(book.copy(progress = sliderProgress))
            }
            onNavigateBack()
        }
    }
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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    // Ícono de Notas
                    IconButton(onClick = { onNavigateToNotes(bookTitle, bookId) }) {
                        Icon(
                            imageVector = Icons.Default.Description, // O Icons.Default.StickyNote2
                            contentDescription = "Ver notas"
                        )
                    }

                    // Ícono de Editar
                    IconButton(onClick = { onNavigateToEdit(bookId) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar detalles"
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, tonalElevation = 3.dp,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showQuoteDialog = true }) {
                            Icon(Icons.Outlined.FormatQuote, null); Spacer(Modifier.width(8.dp)); Text("Citar")
                        }
                        Text("|", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                        TextButton(onClick = { showNoteDialog = true }) {
                            Icon(Icons.Outlined.NoteAdd, null); Spacer(Modifier.width(8.dp)); Text("Anotar")
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Slider(
                            value = sliderProgress,
                            onValueChange = { sliderProgress = it },
                            onValueChangeFinished = {
                                currentProgress = sliderProgress
                                currentBook?.let { book ->
                                    bookViewModel?.updateBook(book.copy(progress = sliderProgress))
                                }
                            },
                            valueRange = 0f..1f,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text("${(sliderProgress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium)                    }
                }
            }
        }
    ) { paddingValues ->
        // 👇 AQUÍ CARGAMOS EL LECTOR DE PDF
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (fileUriString != null) {
                PdfViewer(
                    fileUri = Uri.fromFile(File(fileUriString)),
                    initialPage = initialPageToScroll,
                    onPageCountLoaded = { count ->
                        if (totalPages == 0) totalPages = count  // solo la primera vez
                    },
                    onPageVisible = { visiblePage ->            // 👈 página actual visible
                        if (totalPages > 0) {
                            sliderProgress = visiblePage.toFloat() / totalPages.toFloat()
                        }
                    }
                )
            }
        }
    }
}

// ====================================================================
// MOTOR NATIVO DE LECTURA DE PDF PARA JETPACK COMPOSE
// ====================================================================
@Composable
fun PdfViewer(fileUri: Uri,
              initialPage: Int = 0,
              onPageCountLoaded: (Int) -> Unit = {},
              onPageVisible: (Int) -> Unit = {},
              modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var pdfRenderer by remember { mutableStateOf<PdfRenderer?>(null) }
    var pageCount by remember { mutableIntStateOf(0) }
    var errorLoading by remember { mutableStateOf(false) }

    val renderMutex = remember { Mutex() }

    LaunchedEffect(fileUri) {
        withContext(Dispatchers.IO) {
            try {
                val file = File(fileUri.path ?: return@withContext)
                if (!file.exists() || file.length() == 0L) {
                    errorLoading = true
                    return@withContext
                }
                val fileDescriptor = ParcelFileDescriptor.
                open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                pdfRenderer = PdfRenderer(fileDescriptor)
                pageCount = pdfRenderer?.pageCount ?: 0
                if (pageCount == 0) errorLoading = true
                else onPageCountLoaded(pageCount)  // 👈 NUEVO: notifica el total de páginas
            } catch (e: Exception) {
                android.util.Log.e("PdfViewer", "Error: ${e.message}", e)
                errorLoading = true
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { pdfRenderer?.close() }
    }

    if (errorLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error al cargar el PDF. El archivo puede estar corrupto o protegido.", color = MaterialTheme.colorScheme.error)
        }
    } else if (pdfRenderer != null) {
        val listState = androidx.compose.foundation.lazy.rememberLazyListState()

        LaunchedEffect(initialPage) {
            if (initialPage > 0 && pageCount > 0) {
                listState.scrollToItem(initialPage)
            }
        }

        LaunchedEffect(listState.firstVisibleItemIndex) {
            onPageVisible(listState.firstVisibleItemIndex + 1)  // +1 porque empieza en 0
        }

        LazyColumn(
            state = listState,
            modifier = modifier.fillMaxSize()
        ) {
            items(pageCount) { index ->
                PdfPage(pdfRenderer = pdfRenderer!!, pageIndex = index, renderMutex = renderMutex)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } else {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
                Text("Cargando y procesando libro...")
            }
        }
    }
}

@Composable
fun PdfPage(pdfRenderer: PdfRenderer, pageIndex: Int, renderMutex: Mutex) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(pageIndex) {
        withContext(Dispatchers.IO) {
            renderMutex.withLock {
                try {
                    val page = pdfRenderer.openPage(pageIndex)
                    // Escala x2 para que las letras se vean nítidas al hacer zoom
                    val scale = 2f
                    val bmp = Bitmap.createBitmap(
                        (page.width * scale).toInt(),
                        (page.height * scale).toInt(),
                        Bitmap.Config.ARGB_8888
                    )
                    // Pintamos fondo blanco (los PDF por defecto son transparentes)
                    val canvas = android.graphics.Canvas(bmp)
                    canvas.drawColor(android.graphics.Color.WHITE)

                    page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    page.close()
                    bitmap = bmp
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "Página ${pageIndex + 1}",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    } else {
        Box(modifier = Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator() // Cargando mientras se dibuja
        }
    }
}