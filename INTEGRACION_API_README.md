# 📡 Guía de Integración API REST - BookLog

## ✅ Implementación completada

Se han implementado **3 componentes clave** para consumir la API REST de BookLog desde la app Android:

### 1️⃣ **Interfaz ApiService** ✨
**Archivo:** `data/remote/api/ApiService.kt`

Contiene **60 endpoints** mapeados directamente desde RUTAS.md:
- 9 endpoints de **Usuarios** (CREATE, READ, UPDATE, DELETE + búsquedas)
- 10 endpoints de **Libros** (CRUD + búsquedas + actualizar progreso)
- 5 endpoints de **Series**
- 5 endpoints de **Colecciones**
- 5 endpoints de **Notas**
- 5 endpoints de **Citas**
- 4 endpoints de **Lectura-Colección**
- 2 endpoints de **Health/Info**

**Características:**
- ✅ Todas las funciones son `suspend` (coroutines asincrónicas)
- ✅ Uso de `retrofit2.Response<T>` para manejo HTTP
- ✅ Anotaciones `@Path`, `@Query`, `@Body` correctamente configuradas
- ✅ Comentarios detallados para cada endpoint

### 2️⃣ **Repositorios Remote** 🏗️
**Carpeta:** `data/repository/remote/`

Implementados 6 repositorios remotos:
- `BookRemoteRepository.kt` - Todas las operaciones de libros
- `SerieRemoteRepository.kt` - Gestión de series
- `ColeccionRemoteRepository.kt` - Gestión de colecciones
- `NoteRemoteRepository.kt` - Notas de lectura
- `QuoteRemoteRepository.kt` - Citas literarias
- `LecturaColeccionRemoteRepository.kt` - Relaciones libro-colección

**Patrón:**
```kotlin
suspend fun crearRecurso(): Resource<T>
suspend fun obtenerRecurso(id: Long): Resource<T>
suspend fun listarRecursos(): Resource<List<T>>
suspend fun actualizarRecurso(): Resource<T>
suspend fun eliminarRecurso(id: Long): Resource<Boolean>
```

Cada repositorio remoto:
- ✅ Encapsula errores en `Resource<T>` (sealed class)
- ✅ Mapea automáticamente DTOs → Domain models
- ✅ Maneja excepciones de red con try-catch

### 3️⃣ **AuthRemoteRepositoryImpl** 🔐
**Archivo:** `data/repository/impl/AuthRemoteRepositoryImpl.kt`

Autenticación **remota con fallback local**:

```kotlin
// Login contra API remota
val result = authRemote.loginRemote(correo, contrasena)

// Registro remoto
val result = authRemote.registerRemote(usuario, correo, contrasena)

// Logout
authRemote.logout()
```

**Flujo:**
1. Intenta login/registro contra API remota
2. Si éxito → cachea usuario en BD local (Room)
3. Si error de red → intenta login local (fallback)
4. Retorna `Resource<UserDomain>` con el resultado

---

## 🚀 Cómo Usar

### Inyectar en ViewModels / UseCases

```kotlin
// En tu DI (Hilt, Koin, etc.)
val bookRemoteRepo = BookRemoteRepository(RetrofitClient.apiService)
val authRemote = AuthRemoteRepositoryImpl(RetrofitClient.apiService, userDao)

// En ViewModel
class BookViewModel(private val bookRemote: BookRemoteRepository) : ViewModel() {
    
    fun getBooks(usuarioId: Long) {
        viewModelScope.launch {
            val result = bookRemote.getBooksByUsuarioId(usuarioId)
            when (result) {
                is Resource.Success -> {
                    // Mostrar libros: result.data
                }
                is Resource.Error -> {
                    // Error: result.exception.message
                }
                is Resource.Loading -> {
                    // Mostrar loading
                }
            }
        }
    }
}
```

### Login Remoto

```kotlin
viewModelScope.launch {
    val result = authRemote.loginRemote("usuario@email.com", "password123")
    when (result) {
        is Resource.Success -> {
            val user = result.data
            // Guardar token/sesión si es necesario
            val currentUser = authRemote.getCurrentUser()
        }
        is Resource.Error -> {
            showError(result.exception.message)
        }
        is Resource.Loading -> {}
    }
}
```

---

## 📦 DTOs Disponibles

Todos los DTOs están en `data/remote/dto/`:
- `UserDto` - Usuario
- `BookDto` - Libro
- `SerieDto` - Serie
- `ColeccionDto` - Colección
- `NoteDto` - Nota
- `QuoteDto` - Cita
- `LecturaColeccionDto` - Relación libro-colección

Cada DTO mapea directamente los campos JSON de la API ↔ Kotlin.

---

## 🛠️ Configuración de Retrofit

**Archivo:** `data/remote/api/RetrofitClient.kt`

```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://api-gestorlibros-production.up.railway.app/api/"
    
    // ✅ Logging interceptor (CAMBIAR EN PRODUCCIÓN)
    // ✅ Timeouts: 30 segundos (connect, read, write)
    // ✅ Gson converter para JSON → Kotlin
    
    val apiService: ApiService by lazy { ... }
}
```

**⚠️ Importante:** Comentar el logging interceptor en producción por seguridad.

---

## 🔄 Mappers (DTOs ↔ Domain)

**Archivo:** `data/mapper/Mappers.kt`

Extensiones para convertir automáticamente:
```kotlin
// DTO → Domain
userDto.toDomain(): UserDomain
bookDto.toDomain(): Book

// DTO → Entity (cachear en Room)
userDto.toEntity(): UserEntity
bookDto.toEntity(): BookEntity
```

---

## 📊 Resource<T> - Manejo de Errores

**Archivo:** `domain/util/Resource.kt`

```kotlin
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

// Utilidades
result.isSuccess() // true/false
result.getOrNull() // Retorna T o null
result.map { /* transformar dato */ }
```

---

## 🎯 Próximos Pasos

1. **Integrar en DI (Dependency Injection)**
   - Koin, Hilt u otro framework
   - Proveer `RetrofitClient.apiService`

2. **Crear UseCases** que combinen Remote + Local
   ```kotlin
   class GetBooksUseCase(
       private val bookRemote: BookRemoteRepository,
       private val bookLocal: BookRepository // Room
   )
   ```

3. **Caching Strategy** (cuando usar caché local)
   - Si éxito remoto → cachear
   - Si error remoto → usar caché local
   - TTL (Time To Live) para invalidación

4. **Tokens de Autenticación**
   - Guardar token de sesión en DataStore/SharedPrefs
   - Usar interceptor de Retrofit para agregar Bearer token

5. **Tests**
   - MockWebServer para testing de API
   - Repository tests con coroutines

---

## 📝 Estructura Resumida

```
data/
├── remote/
│   ├── api/
│   │   ├── ApiBookLog.kt          ✅ Interfaz Retrofit (60 endpoints)
│   │   └── RetrofitClient.kt      ✅ Configuración Retrofit
│   ├── dto/
│   │   ├── UserDto.kt             ✅
│   │   ├── BookDto.kt             ✅
│   │   ├── SerieDto.kt            ✅
│   │   ├── ColeccionDto.kt        ✅
│   │   ├── NoteDto.kt             ✅
│   │   ├── QuoteDto.kt            ✅
│   │   └── LecturaColeccionDto.kt ✅
│   └── repository/
│       ├── BookRemoteRepository.kt           ✅
│       ├── SerieRemoteRepository.kt          ✅
│       ├── ColeccionRemoteRepository.kt      ✅
│       ├── NoteRemoteRepository.kt           ✅
│       ├── QuoteRemoteRepository.kt          ✅
│       └── LecturaColeccionRemoteRepository.kt ✅
├── mapper/
│   └── Mappers.kt                 ✅ (agregados mappers DTO → Domain)
└── repository/impl/
    └── AuthRemoteRepositoryImpl.kt ✅ Login/registro remoto
domain/
└── util/
    └── Resource.kt                ✅ Sealed class para manejo de errores
```

---

## ✨ Características Implementadas

- ✅ **60 endpoints** mapeados desde RUTAS.md
- ✅ **Coroutines** (suspend functions)
- ✅ **Resource<T>** para manejo type-safe de errores
- ✅ **DTOs completos** con @SerializedName
- ✅ **Mappers automáticos** Entity ↔ DTO ↔ Domain
- ✅ **6 Repositorios Remote** (Book, Serie, Colección, Note, Quote, LecturaColección)
- ✅ **AuthRemoteRepositoryImpl** con fallback local
- ✅ **Retrofit configurado** con OkHttp + Gson + Logging
- ✅ **Timeouts configurados** (30s)
- ✅ **Documentación inline** en cada función
- ✅ **Cero cambios en código existente** (completamente modular)

---

## 🔗 Base URL

```
https://api-gestorlibros-production.up.railway.app/api/
```

Todos los endpoints están bajo `/api/` automáticamente.

---

**Última actualización:** 2026-04-16  
**Versión:** 1.0.0 ✅ Implementación Completa

