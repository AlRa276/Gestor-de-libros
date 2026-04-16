# 🏗️ DIAGRAMA DE ARQUITECTURA - API REST Integration

## ARQUITECTURA EN CAPAS

```
┌─────────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER                        │
│                                                                  │
│   Activity/Fragment → ViewModel → UI State                       │
│   (ExampleBookViewModel.kt)                                      │
└──────────────────────────────┬──────────────────────────────────┘
                               │ viewModelScope.launch
                               ↓
┌─────────────────────────────────────────────────────────────────┐
│                         DOMAIN LAYER                             │
│                                                                  │
│   UseCase / Interactor                                           │
│   ├─ Resource<T> (sealed class)                                 │
│   ├─ UserDomain, Book, SerieDomain, ColeccionDomain             │
│   └─ NoteDomain, QuoteDomain                                    │
│   (domain/model/ & domain/util/Resource.kt)                     │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                ┌──────────────┴──────────────┐
                ↓                             ↓
┌─────────────────────────────┐  ┌──────────────────────────────────┐
│    DATA LAYER - REMOTE      │  │  DATA LAYER - LOCAL (Room)       │
│                             │  │                                  │
│ BookRemoteRepository        │  │ BookRepository (existing)        │
│ SerieRemoteRepository       │  │ UserRepository (existing)        │
│ ColeccionRemoteRepository   │  │ SerieRepository (existing)       │
│ NoteRemoteRepository        │  │ ColeccionRepository (existing)   │
│ QuoteRemoteRepository       │  │ etc.                             │
│ LecturaColeccionRemote...   │  │                                  │
│ AuthRemoteRepositoryImpl     │  │ ┌──────────────────────────────┐ │
│                             │  │ │ UserEntity, BookEntity, etc  │ │
│ ├─ Mappers (DTO → Domain)   │  │ └──────────────────────────────┘ │
│ └─ Manejo Resource<T>       │  │                                  │
│ (data/repository/remote/)   │  │ (data/repository/impl/ +        │
│                             │  │  data/local/entity/)            │
└──────────────┬──────────────┘  └──────────────────────────────────┘
               │
       ┌───────┴───────┐
       ↓               ↓
   ┌────────────────────────────────────────┐
   │      RETROFIT CLIENT CONFIGURATION     │
   │                                        │
   │ RetrofitClient (singleton)             │
   │ ├─ OkHttpClient                        │
   │ ├─ HttpLoggingInterceptor              │
   │ ├─ Timeout: 30s                        │
   │ └─ GsonConverterFactory                │
   │ (data/remote/api/RetrofitClient.kt)    │
   └──────────────┬─────────────────────────┘
                  │
       ┌──────────┴──────────┐
       ↓                     ↓
   ┌──────────────────────────────────────┐
   │    API SERVICE INTERFACE (Retrofit)  │
   │                                      │
   │  ApiService (apiBookLog.kt)          │
   │  ├─ 9 endpoints Usuarios             │
   │  ├─ 10 endpoints Libros              │
   │  ├─ 5 endpoints Series               │
   │  ├─ 5 endpoints Colecciones          │
   │  ├─ 5 endpoints Notas                │
   │  ├─ 5 endpoints Citas                │
   │  ├─ 4 endpoints Lectura-Colección    │
   │  └─ 2 endpoints Health/Info          │
   │ (data/remote/api/ApiBookLog.kt)      │
   └──────────────┬─────────────────────────┘
                  │
       ┌──────────┴──────────┐
       ↓                     ↓
   ┌───────────────────────────────────┐
   │         DATA TRANSFER OBJECTS      │
   │                                   │
   │ UserDto, BookDto, SerieDto,        │
   │ ColeccionDto, NoteDto, QuoteDto,   │
   │ LecturaColeccionDto                │
   │ (data/remote/dto/*.kt)             │
   └──────────────┬────────────────────┘
                  │
                  ↓
   ┌─────────────────────────────────────┐
   │   RAILWAY API - REMOTE SERVER       │
   │                                     │
   │ https://api-gestorlibros...         │
   │ /api/users                          │
   │ /api/books                          │
   │ /api/series                         │
   │ /api/colecciones                    │
   │ /api/notes                          │
   │ /api/quotes                         │
   │ /api/lectura-coleccion              │
   │ /api/health                         │
   └─────────────────────────────────────┘
```

---

## FLUJO: LOGIN REMOTO CON FALLBACK LOCAL

```
┌─────────────────┐
│  Usuario ingresa│
│  credenciales   │
└────────┬────────┘
         │
         ↓
┌──────────────────────────────────┐
│ AuthRemoteRepositoryImpl.          │
│ loginRemote(correo, contrasena)  │
└────────┬─────────────────────────┘
         │
         ↓
    ┌────────┐
    │ Try:   │
    └───┬────┘
        │
        ↓
    ┌──────────────────────────────┐
    │ apiService.getUserByEmail()  │ → API REMOTA
    └────────┬─────────────────────┘
             │
             ├─── ✅ Success ────┐
             │                   ↓
             │          ┌─────────────────┐
             │          │ Validar hash    │
             │          │ contraseña      │
             │          └────┬────────┬───┘
             │               │        │
             │           ✅OK │        ❌FAIL
             │               │        │
             │               ↓        ↓
             │          ┌────────┐ ┌─────────┐
             │          │Cachear │ │Retornar │
             │          │en Room │ │  Error  │
             │          └────┬───┘ └─────────┘
             │               │
             │               ↓
             │          ┌──────────────┐
             │          │Retornar      │
             │          │Resource.     │
             │          │Success<User> │
             │          └──────────────┘
             │
             ├─── ❌ Catch (Network Error)
             │
             ↓
         ┌──────────────────────────┐
         │ loginLocal(correo,       │
         │ contrasena) → BD LOCAL   │
         └────────┬─────────────────┘
                  │
                  ├─── ✅ Found ────┐
                  │                 ↓
                  │          ┌─────────────────┐
                  │          │Validar hash     │
                  │          │contraseña       │
                  │          └────┬────────┬───┘
                  │               │        │
                  │           ✅OK │        ❌FAIL
                  │               │        │
                  │               ↓        ↓
                  │          ┌────────┐ ┌──────────┐
                  │          │Retornar│ │Retornar  │
                  │          │Success │ │  Error   │
                  │          └────────┘ └──────────┘
                  │
                  └─── ❌ Not Found
                  
                  ↓
            ┌─────────────────┐
            │ Retornar Error  │
            │ "No encontrado" │
            └─────────────────┘
```

---

## FLUJO: OBTENER LIBROS

```
┌──────────────────────────────────┐
│ BookViewModel.loadBooks()        │
│ viewModelScope.launch { }        │
└────────┬─────────────────────────┘
         │
         ↓
┌──────────────────────────────────────────┐
│ BookRemoteRepository.                    │
│ getBooksByUsuarioId(usuarioId)          │
└────────┬─────────────────────────────────┘
         │
         ↓
    ┌────────┐
    │ Try:   │
    └───┬────┘
        │
        ↓
┌────────────────────────────────────────┐
│ apiService.getBooksByUsuarioId()       │ → API REMOTA
└────────┬─────────────────────────────────┘
         │
         ├─── ✅ Response OK
         │
         ↓
┌────────────────────────────┐
│ response.body() != null?   │
└────┬───────────────┬───────┘
     │ ✅ YES        │ ❌ NO
     ↓               ↓
┌─────────────┐  ┌──────────────┐
│ bookDtos    │  │ Retornar     │
│ recibidas   │  │ Resource.    │
└────┬────────┘  │ Error(...)   │
     │           └──────────────┘
     ↓
┌──────────────────────────────┐
│ Mapear BookDto[] →           │
│ Book[] (toDomain())          │
└────┬─────────────────────────┘
     │
     ↓
┌──────────────────────────────┐
│ Retornar                     │
│ Resource.Success(books)      │
└──────────────────────────────┘
     │
     ↓
┌──────────────────────────────┐
│ ViewModel.when result        │
└────┬─────────────────────────┘
     │
     ├─→ Resource.Success
     │    └─→ Actualizar UI con libros
     │
     ├─→ Resource.Error
     │    └─→ Mostrar toast error
     │
     └─→ Resource.Loading
          └─→ Mostrar spinner

    ❌ Catch (Network Error)
    ↓
    Retornar Resource.Error(exception)
```

---

## FLUJO: CREAR LIBRO

```
┌─────────────────────────────────┐
│ ViewModel.crearLibro()          │
│ (con datos del formulario)      │
└────────┬────────────────────────┘
         │
         ↓
┌──────────────────────────────────┐
│ Crear objeto Book                │
│ con usuario actual               │
└────────┬─────────────────────────┘
         │
         ↓
┌──────────────────────────────────────┐
│ BookRemoteRepository.createBook()    │
└────────┬─────────────────────────────┘
         │
         ↓
┌──────────────────────────────────┐
│ Mapear Book → BookDto            │
└────────┬─────────────────────────┘
         │
         ↓
┌──────────────────────────────────┐
│ apiService.createBook(bookDto)   │ → API REMOTA (POST)
└────────┬─────────────────────────┘
         │
         ├─── ✅ Response OK
         │
         ↓
┌──────────────────────────────┐
│ BookDto creado recibido      │
└────────┬─────────────────────┘
         │
         ↓
┌──────────────────────────────┐
│ Mapear BookDto → Book        │
│ (con ID asignado por API)    │
└────────┬─────────────────────┘
         │
         ↓
┌──────────────────────────────┐
│ Resource.Success(bookCreado) │
└────────┬─────────────────────┘
         │
         ↓
┌──────────────────────────────┐
│ Recargar lista de libros     │
│ o agregar a lista local      │
└──────────────────────────────┘
```

---

## COMPONENTES PRINCIPALES

```
┌─ UI Layer
│  ├─ Activity/Fragment
│  └─ ViewModel
│
├─ Domain Layer
│  ├─ Models (UserDomain, Book, etc)
│  ├─ UseCase
│  └─ Resource<T> (error handling)
│
├─ Data Layer (Local)
│  ├─ Repository (impl)
│  ├─ DAO
│  └─ Entity
│
├─ Data Layer (Remote)
│  ├─ Repository (remote)
│  ├─ DTOs
│  └─ API Service
│
└─ Network Layer
   ├─ Retrofit
   ├─ OkHttp
   ├─ HttpLoggingInterceptor
   └─ Gson
```

---

## INYECCIÓN DE DEPENDENCIAS

```
┌─────────────────────────────────────┐
│        ACTIVITY / FRAGMENT          │
└──────────────┬──────────────────────┘
               │
               ↓
        ┌──────────────┐
        │ RemoteModule │ ← RemoteModule.kt
        │ (Providers)  │
        └──────────────┘
         │            │
         ├─→ provideApiService()
         │   └─→ RetrofitClient.apiService
         │
         ├─→ provideAuthRemoteRepository()
         │   └─→ AuthRemoteRepositoryImpl
         │
         ├─→ provideBookRemoteRepository()
         │   └─→ BookRemoteRepository
         │
         ├─→ provideSerieRemoteRepository()
         │   └─→ SerieRemoteRepository
         │
         └─→ [otros repositorios...]
```

---

## FLUJO DE DATOS: REQUEST → RESPONSE

```
┌──────────────┐
│   ViewModel  │
└──────┬───────┘
       │ Llama: bookRemote.getBooks(id)
       ↓
┌─────────────────────────┐
│  BookRemoteRepository   │
└──────┬──────────────────┘
       │ Construye params
       ↓
┌─────────────────────────┐
│    ApiService (Retrofit)│
└──────┬──────────────────┘
       │ Serializa con Gson
       ↓
┌─────────────────────────┐
│   OkHttpClient          │
│   - Headers             │
│   - Logging Interceptor │
│   - Timeout 30s         │
└──────┬──────────────────┘
       │ HTTP GET Request
       ↓
    ╔═════════════════════════════════════╗
    ║   RAILWAY API PRODUCTION SERVER     ║
    ║ /api/books/usuario/{usuarioId}     ║
    ╚═════════════════════════════════════╝
       │ HTTP 200 OK + JSON
       ↓
┌─────────────────────────┐
│   OkHttpClient (Rx)     │
│   - Logging response    │
└──────┬──────────────────┘
       │ JSON String
       ↓
┌─────────────────────────┐
│   Gson Converter        │
│   JSON → BookDto[]      │
└──────┬──────────────────┘
       │ BookDto[]
       ↓
┌─────────────────────────┐
│  BookRemoteRepository   │
│  BookDto[] → Book[]     │
│  (mappers)              │
└──────┬──────────────────┘
       │ Book[]
       ↓
┌─────────────────────────┐
│  Resource.Success<>     │
└──────┬──────────────────┘
       │ 
       ↓
┌─────────────────────────┐
│  ViewModel cuando       │
│  Resource.Success       │
│  Actualizar UI          │
└─────────────────────────┘
```

---

## ESTADOS POSIBLES

```
Resource<T>
│
├─ Success(data: T)
│  └─ La operación fue exitosa
│     └─ result.data contiene el resultado
│
├─ Error(exception: Exception)
│  └─ La operación falló
│     └─ result.exception.message tiene el error
│
└─ Loading
   └─ La operación está en progreso
      └─ Mostrar spinner/progress bar
```

---

**Última actualización:** 2026-04-16  
**Versión:** 1.0.0

