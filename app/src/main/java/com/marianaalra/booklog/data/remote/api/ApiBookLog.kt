package com.marianaalra.booklog.data.remote.api

import com.marianaalra.booklog.data.remote.dto.BookDto
import com.marianaalra.booklog.data.remote.dto.ColeccionDto
import com.marianaalra.booklog.data.remote.dto.LecturaColeccionDto
import com.marianaalra.booklog.data.remote.dto.NoteDto
import com.marianaalra.booklog.data.remote.dto.QuoteDto
import com.marianaalra.booklog.data.remote.dto.SerieDto
import com.marianaalra.booklog.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz Retrofit que define todas las operaciones de la API REST de BookLog.
 * Base URL: https://api-gestorlibros-production.up.railway.app/api
 *
 * Cada función está marcada como "suspend" para permitir coroutines asincrónicas.
 * Las respuestas se encapsulan en retrofit2.Response<T> para manejar códigos HTTP.
 */
interface ApiService {

    // ==================== USUARIOS (9 rutas) ====================

    /**
     * POST /api/users
     * Crear un nuevo usuario.
     */
    @POST("users")
    suspend fun createUser(@Body user: UserDto): Response<UserDto>

    /**
     * GET /api/users
     * Obtener todos los usuarios.
     */
    @GET("users")
    suspend fun getAllUsers(): Response<List<UserDto>>

    /**
     * GET /api/users/{id}
     * Obtener un usuario por su ID.
     */
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<UserDto>

    /**
     * GET /api/users/by-username/{nombreUsuario}
     * Buscar usuario por nombre de usuario.
     */
    @GET("users/by-username/{nombreUsuario}")
    suspend fun getUserByUsername(@Path("nombreUsuario") nombreUsuario: String): Response<UserDto>

    /**
     * GET /api/users/by-email/{correo}
     * Buscar usuario por correo.
     */
    @GET("users/by-email/{correo}")
    suspend fun getUserByEmail(@Path("correo") correo: String): Response<UserDto>

    /**
     * GET /api/users/exists/username/{nombreUsuario}
     * Verificar si existe un usuario con ese nombre.
     */
    @GET("users/exists/username/{nombreUsuario}")
    suspend fun existsUsername(@Path("nombreUsuario") nombreUsuario: String): Response<Boolean>

    /**
     * GET /api/users/exists/email/{correo}
     * Verificar si existe un usuario con ese correo.
     */
    @GET("users/exists/email/{correo}")
    suspend fun existsEmail(@Path("correo") correo: String): Response<Boolean>

    /**
     * PUT /api/users/{id}
     * Actualizar un usuario existente.
     */
    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body user: UserDto
    ): Response<UserDto>

    /**
     * DELETE /api/users/{id}
     * Eliminar un usuario.
     */
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Long): Response<Map<String, String>>

    // ==================== LIBROS (10 rutas) ====================

    /**
     * POST /api/books
     * Crear un nuevo libro.
     */
    @POST("books")
    suspend fun createBook(@Body book: BookDto): Response<BookDto>

    /**
     * GET /api/books/{id}
     * Obtener un libro por su ID.
     */
    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: Long): Response<BookDto>

    /**
     * GET /api/books/usuario/{usuarioId}
     * Obtener todos los libros de un usuario.
     */
    @GET("books/usuario/{usuarioId}")
    suspend fun getBooksByUsuarioId(@Path("usuarioId") usuarioId: Long): Response<List<BookDto>>

    /**
     * GET /api/books/usuario/{usuarioId}/estado/{estado}
     * Obtener libros de un usuario filtrados por estado.
     */
    @GET("books/usuario/{usuarioId}/estado/{estado}")
    suspend fun getBooksByEstado(
        @Path("usuarioId") usuarioId: Long,
        @Path("estado") estado: String
    ): Response<List<BookDto>>

    /**
     * GET /api/books/serie/{serieId}
     * Obtener todos los libros de una serie.
     */
    @GET("books/serie/{serieId}")
    suspend fun getBooksBySerieId(@Path("serieId") serieId: Long): Response<List<BookDto>>

    /**
     * GET /api/books/search/titulo?usuarioId=X&titulo=Y
     * Buscar libros por título.
     */
    @GET("books/search/titulo")
    suspend fun searchByTitulo(
        @Query("usuarioId") usuarioId: Long,
        @Query("titulo") titulo: String
    ): Response<List<BookDto>>

    /**
     * GET /api/books/search/autor?usuarioId=X&autor=Y
     * Buscar libros por autor.
     */
    @GET("books/search/autor")
    suspend fun searchByAutor(
        @Query("usuarioId") usuarioId: Long,
        @Query("autor") autor: String
    ): Response<List<BookDto>>

    /**
     * PUT /api/books/{id}
     * Actualizar un libro existente.
     */
    @PUT("books/{id}")
    suspend fun updateBook(
        @Path("id") id: Long,
        @Body book: BookDto
    ): Response<BookDto>

    /**
     * PATCH /api/books/{id}/progress?progreso=X
     * Actualizar solo el progreso de lectura de un libro.
     */
    @PATCH("books/{id}/progress")
    suspend fun updateBookProgress(
        @Path("id") id: Long,
        @Query("progreso") progreso: Float
    ): Response<BookDto>

    /**
     * DELETE /api/books/{id}
     * Eliminar un libro.
     */
    @DELETE("books/{id}")
    suspend fun deleteBook(@Path("id") id: Long): Response<Map<String, String>>

    // ==================== SERIES (5 rutas) ====================

    /**
     * POST /api/series
     * Crear una nueva serie.
     */
    @POST("series")
    suspend fun createSerie(@Body serie: SerieDto): Response<SerieDto>

    /**
     * GET /api/series/{id}
     * Obtener una serie por su ID.
     */
    @GET("series/{id}")
    suspend fun getSerieById(@Path("id") id: Long): Response<SerieDto>

    /**
     * GET /api/series/usuario/{usuarioId}
     * Obtener todas las series de un usuario.
     */
    @GET("series/usuario/{usuarioId}")
    suspend fun getSeriesByUsuarioId(@Path("usuarioId") usuarioId: Long): Response<List<SerieDto>>

    /**
     * PUT /api/series/{id}
     * Actualizar una serie existente.
     */
    @PUT("series/{id}")
    suspend fun updateSerie(
        @Path("id") id: Long,
        @Body serie: SerieDto
    ): Response<SerieDto>

    /**
     * DELETE /api/series/{id}
     * Eliminar una serie.
     */
    @DELETE("series/{id}")
    suspend fun deleteSerie(@Path("id") id: Long): Response<Map<String, String>>

    // ==================== COLECCIONES (5 rutas) ====================

    /**
     * POST /api/colecciones
     * Crear una nueva colección.
     */
    @POST("colecciones")
    suspend fun createColeccion(@Body coleccion: ColeccionDto): Response<ColeccionDto>

    /**
     * GET /api/colecciones/{id}
     * Obtener una colección por su ID.
     */
    @GET("colecciones/{id}")
    suspend fun getColeccionById(@Path("id") id: Long): Response<ColeccionDto>

    /**
     * GET /api/colecciones/usuario/{usuarioId}
     * Obtener todas las colecciones de un usuario.
     */
    @GET("colecciones/usuario/{usuarioId}")
    suspend fun getColeccionesByUsuarioId(@Path("usuarioId") usuarioId: Long): Response<List<ColeccionDto>>

    /**
     * PUT /api/colecciones/{id}
     * Actualizar una colección existente.
     */
    @PUT("colecciones/{id}")
    suspend fun updateColeccion(
        @Path("id") id: Long,
        @Body coleccion: ColeccionDto
    ): Response<ColeccionDto>

    /**
     * DELETE /api/colecciones/{id}
     * Eliminar una colección.
     */
    @DELETE("colecciones/{id}")
    suspend fun deleteColeccion(@Path("id") id: Long): Response<Map<String, String>>

    // ==================== NOTAS (5 rutas) ====================

    /**
     * POST /api/notes
     * Crear una nueva nota.
     */
    @POST("notes")
    suspend fun createNote(@Body note: NoteDto): Response<NoteDto>

    /**
     * GET /api/notes/{id}
     * Obtener una nota por su ID.
     */
    @GET("notes/{id}")
    suspend fun getNoteById(@Path("id") id: Long): Response<NoteDto>

    /**
     * GET /api/notes/lectura/{lecturaId}
     * Obtener todas las notas de un libro.
     */
    @GET("notes/lectura/{lecturaId}")
    suspend fun getNotesByLecturaId(@Path("lecturaId") lecturaId: Long): Response<List<NoteDto>>

    /**
     * PUT /api/notes/{id}
     * Actualizar una nota existente.
     */
    @PUT("notes/{id}")
    suspend fun updateNote(
        @Path("id") id: Long,
        @Body note: NoteDto
    ): Response<NoteDto>

    /**
     * DELETE /api/notes/{id}
     * Eliminar una nota.
     */
    @DELETE("notes/{id}")
    suspend fun deleteNote(@Path("id") id: Long): Response<Map<String, String>>

    // ==================== CITAS (5 rutas) ====================

    /**
     * POST /api/quotes
     * Crear una nueva cita.
     */
    @POST("quotes")
    suspend fun createQuote(@Body quote: QuoteDto): Response<QuoteDto>

    /**
     * GET /api/quotes/{id}
     * Obtener una cita por su ID.
     */
    @GET("quotes/{id}")
    suspend fun getQuoteById(@Path("id") id: Long): Response<QuoteDto>

    /**
     * GET /api/quotes/lectura/{lecturaId}
     * Obtener todas las citas de un libro.
     */
    @GET("quotes/lectura/{lecturaId}")
    suspend fun getQuotesByLecturaId(@Path("lecturaId") lecturaId: Long): Response<List<QuoteDto>>

    /**
     * PUT /api/quotes/{id}
     * Actualizar una cita existente.
     */
    @PUT("quotes/{id}")
    suspend fun updateQuote(
        @Path("id") id: Long,
        @Body quote: QuoteDto
    ): Response<QuoteDto>

    /**
     * DELETE /api/quotes/{id}
     * Eliminar una cita.
     */
    @DELETE("quotes/{id}")
    suspend fun deleteQuote(@Path("id") id: Long): Response<Map<String, String>>

    // ==================== LECTURA-COLECCIÓN (4 rutas) ====================

    /**
     * POST /api/lectura-coleccion
     * Agregar un libro a una colección.
     */
    @POST("lectura-coleccion")
    suspend fun addBookToColeccion(@Body lecturaColeccion: LecturaColeccionDto): Response<LecturaColeccionDto>

    /**
     * GET /api/lectura-coleccion/coleccion/{coleccionId}
     * Obtener todos los libros de una colección.
     */
    @GET("lectura-coleccion/coleccion/{coleccionId}")
    suspend fun getBooksByColeccion(@Path("coleccionId") coleccionId: Long): Response<List<BookDto>>

    /**
     * GET /api/lectura-coleccion/lectura/{lecturaId}
     * Obtener todas las colecciones a las que pertenece un libro.
     */
    @GET("lectura-coleccion/lectura/{lecturaId}")
    suspend fun getColeccionesByBook(@Path("lecturaId") lecturaId: Long): Response<List<ColeccionDto>>

    /**
     * DELETE /api/lectura-coleccion/{lecturaId}/{coleccionId}
     * Remover un libro de una colección.
     */
    @DELETE("lectura-coleccion/{lecturaId}/{coleccionId}")
    suspend fun removeBookFromColeccion(
        @Path("lecturaId") lecturaId: Long,
        @Path("coleccionId") coleccionId: Long
    ): Response<Map<String, String>>

    // ==================== HEALTH & INFO (2 rutas) ====================

    /**
     * GET /api/health
     * Verificar que la API está disponible.
     */
    @GET("health")
    suspend fun health(): Response<Map<String, String>>

    /**
     * GET /api/info
     * Obtener información de la API.
     */
    @GET("info")
    suspend fun info(): Response<Map<String, String>>
}
