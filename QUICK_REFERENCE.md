# 🚀 QUICK REFERENCE - Fragmentos de Código Listos para Usar

## 1️⃣ LOGIN REMOTO

```kotlin
// En tu Activity/ViewModel
val authRemote = AuthRemoteRepositoryImpl(
    RetrofitClient.apiService,
    database.userDao()
)

viewModelScope.launch {
    val result = authRemote.loginRemote(correo, contrasena)
    when (result) {
        is Resource.Success -> {
            val usuario = result.data
            Log.d("Login", "Bienvenido ${usuario.nombreUsuario}")
            navegar a pantalla principal
        }
        is Resource.Error -> {
            Log.e("Login", "Error: ${result.exception.message}")
            mostrar toast con error
        }
        is Resource.Loading -> {
            mostrar loading spinner
        }
    }
}
```

---

## 2️⃣ REGISTRO REMOTO

```kotlin
val authRemote = AuthRemoteRepositoryImpl(
    RetrofitClient.apiService,
    database.userDao()
)

viewModelScope.launch {
    val result = authRemote.registerRemote(usuario, correo, contrasena)
    when (result) {
        is Resource.Success -> {
            val usuarioNuevo = result.data
            Log.d("Register", "Usuario creado: ${usuarioNuevo.id}")
        }
        is Resource.Error -> {
            Log.e("Register", result.exception.message)
        }
        is Resource.Loading -> {}
    }
}
```

---

## 3️⃣ OBTENER LIBROS DEL USUARIO

```kotlin
val bookRemote = BookRemoteRepository(RetrofitClient.apiService)

viewModelScope.launch {
    val result = bookRemote.getBooksByUsuarioId(usuarioId)
    when (result) {
        is Resource.Success -> {
            val libros = result.data // List<Book>
            actualizarUI(libros)
        }
        is Resource.Error -> {
            mostrarError(result.exception.message)
        }
        is Resource.Loading -> {}
    }
}
```

---

## 4️⃣ OBTENER LIBROS POR ESTADO

```kotlin
val result = bookRemote.getBooksByEstado(usuarioId, "LEYENDO")
// O: "PENDIENTE", "COMPLETADO", etc.
```

---

## 5️⃣ BUSCAR LIBROS

```kotlin
// Por título
val result = bookRemote.searchByTitulo(usuarioId, "Harry Potter")

// Por autor
val result = bookRemote.searchByAutor(usuarioId, "J.K. Rowling")
```

---

## 6️⃣ CREAR LIBRO

```kotlin
val nuevoLibro = Book(
    usuarioId = userId,
    title = "1984",
    author = "George Orwell",
    fileFormat = "PDF",
    fileUri = "/ruta/del/archivo.pdf",
    nombreArchivo = "1984.pdf",
    status = "PENDIENTE",
    progress = 0f,
    coverPath = "/covers/1984.jpg"
)

viewModelScope.launch {
    val result = bookRemote.createBook(nuevoLibro)
    when (result) {
        is Resource.Success -> {
            val libroCreado = result.data
            Log.d("Create", "Libro creado con ID: ${libroCreado.id}")
        }
        is Resource.Error -> {
            Log.e("Create", result.exception.message)
        }
        is Resource.Loading -> {}
    }
}
```

---

## 7️⃣ ACTUALIZAR LIBRO

```kotlin
val libroActualizado = libroExistente.copy(
    title = "Nuevo título",
    author = "Nuevo autor",
    status = "LEYENDO"
)

viewModelScope.launch {
    val result = bookRemote.updateBook(libroActualizado)
    when (result) {
        is Resource.Success -> {
            Log.d("Update", "Libro actualizado")
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 8️⃣ ACTUALIZAR PROGRESO

```kotlin
viewModelScope.launch {
    val result = bookRemote.updateBookProgress(bookId, 45.5f)
    when (result) {
        is Resource.Success -> {
            Log.d("Progress", "Progreso: 45.5%")
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 9️⃣ ELIMINAR LIBRO

```kotlin
viewModelScope.launch {
    val result = bookRemote.deleteBook(bookId)
    when (result) {
        is Resource.Success -> {
            Log.d("Delete", "Libro eliminado")
            recargarLista()
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 🔟 SERIES - CREAR

```kotlin
val nuevaSerie = SerieDomain(
    usuarioId = userId,
    nombre = "Harry Potter"
)

val serieRemote = SerieRemoteRepository(RetrofitClient.apiService)

viewModelScope.launch {
    val result = serieRemote.createSerie(nuevaSerie)
    when (result) {
        is Resource.Success -> {
            val serieCreada = result.data
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 1️⃣1️⃣ SERIES - OBTENER POR USUARIO

```kotlin
val serieRemote = SerieRemoteRepository(RetrofitClient.apiService)

viewModelScope.launch {
    val result = serieRemote.getSeriesByUsuarioId(usuarioId)
    when (result) {
        is Resource.Success -> {
            val series = result.data // List<SerieDomain>
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 1️⃣2️⃣ LIBROS DE UNA SERIE

```kotlin
viewModelScope.launch {
    val result = bookRemote.getBooksBySerieId(serieId)
    when (result) {
        is Resource.Success -> {
            val librosDelaSerie = result.data
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 1️⃣3️⃣ COLECCIONES - CREAR

```kotlin
val nuevaColeccion = ColeccionDomain(
    usuarioId = userId,
    nombre = "Mis favoritos"
)

val coleccionRemote = ColeccionRemoteRepository(RetrofitClient.apiService)

viewModelScope.launch {
    val result = coleccionRemote.createColeccion(nuevaColeccion)
    when (result) {
        is Resource.Success -> {
            val coleccionCreada = result.data
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 1️⃣4️⃣ AGREGAR LIBRO A COLECCIÓN

```kotlin
val lectura_coleccionRemote = LecturaColeccionRemoteRepository(RetrofitClient.apiService)

viewModelScope.launch {
    val result = lectura_coleccionRemote.addBookToColeccion(
        lecturaId = bookId,
        coleccionId = coleccionId
    )
    when (result) {
        is Resource.Success -> {
            Log.d("Add", "Libro agregado a colección")
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 1️⃣5️⃣ OBTENER LIBROS DE UNA COLECCIÓN

```kotlin
viewModelScope.launch {
    val result = lectura_coleccionRemote.getBooksByColeccion(coleccionId)
    when (result) {
        is Resource.Success -> {
            val librosEnColeccion = result.data // List<Book>
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 1️⃣6️⃣ NOTAS - CREAR

```kotlin
val nuevaNota = NoteDomain(
    bookId = lecturaId,
    contenido = "Este capítulo es muy interesante",
    referenciaPagina = "p. 45"
)

val noteRemote = NoteRemoteRepository(RetrofitClient.apiService)

viewModelScope.launch {
    val result = noteRemote.createNote(nuevaNota)
    when (result) {
        is Resource.Success -> {
            val notaCreada = result.data
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 1️⃣7️⃣ OBTENER NOTAS DE UN LIBRO

```kotlin
viewModelScope.launch {
    val result = noteRemote.getNotesByLecturaId(bookId)
    when (result) {
        is Resource.Success -> {
            val notas = result.data // List<NoteDomain>
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 1️⃣8️⃣ CITAS - CREAR

```kotlin
val nuevaCita = QuoteDomain(
    bookId = lecturaId,
    textoCitado = "La vida es como un libro sin final",
    referenciaPagina = "p. 72",
    comentario = "Frase inspiradora"
)

val quoteRemote = QuoteRemoteRepository(RetrofitClient.apiService)

viewModelScope.launch {
    val result = quoteRemote.createQuote(nuevaCita)
    when (result) {
        is Resource.Success -> {
            val citaCreada = result.data
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 1️⃣9️⃣ OBTENER CITAS DE UN LIBRO

```kotlin
viewModelScope.launch {
    val result = quoteRemote.getQuotesByLecturaId(bookId)
    when (result) {
        is Resource.Success -> {
            val citas = result.data // List<QuoteDomain>
        }
        is Resource.Error -> {}
        is Resource.Loading -> {}
    }
}
```

---

## 2️⃣0️⃣ LOGOUT

```kotlin
viewModelScope.launch {
    authRemote.logout()
    Log.d("Logout", "Sesión cerrada")
    navegar a login
}
```

---

## 🎯 PLANTILLA GENÉRICA PARA CUALQUIER OPERACIÓN

```kotlin
viewModelScope.launch {
    val result = repository.operacion(parametros)
    
    when (result) {
        is Resource.Success -> {
            val datos = result.data
            // ✅ Operación exitosa
            actualizarUI(datos)
        }
        is Resource.Error -> {
            // ❌ Error
            val mensajeError = result.exception.message
            mostrarToast(mensajeError)
            Log.e("Error", mensajeError, result.exception)
        }
        is Resource.Loading -> {
            // ⏳ Cargando
            mostrarLoadingDialog()
        }
    }
}
```

---

## 📌 IMPORTS NECESARIOS

```kotlin
// Para usar en tus archivos
import com.marianaalra.booklog.data.remote.api.RetrofitClient
import com.marianaalra.booklog.data.repository.impl.AuthRemoteRepositoryImpl
import com.marianaalra.booklog.data.repository.remote.BookRemoteRepository
import com.marianaalra.booklog.data.repository.remote.SerieRemoteRepository
import com.marianaalra.booklog.data.repository.remote.ColeccionRemoteRepository
import com.marianaalra.booklog.data.repository.remote.NoteRemoteRepository
import com.marianaalra.booklog.data.repository.remote.QuoteRemoteRepository
import com.marianaalra.booklog.data.repository.remote.LecturaColeccionRemoteRepository
import com.marianaalra.booklog.domain.util.Resource
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.model.UserDomain
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
```

---

## ✨ CONSEJOS PRÁCTICOS

1. **Siempre usa viewModelScope.launch** para coroutines
2. **Maneja los 3 estados**: Success, Error, Loading
3. **Log todo** para debugging (especialmente errores)
4. **Cachea en Room** cuando login/registro sean exitosos
5. **Muestra spinners** en estado Loading
6. **Muestra toasts/snackbars** en estado Error
7. **Actualiza UI** solo en estado Success

---

**¡Copia, pega y adapta según tus necesidades! 🚀**

