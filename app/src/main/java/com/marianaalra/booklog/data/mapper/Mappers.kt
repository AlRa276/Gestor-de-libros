package com.marianaalra.booklog.data.mapper

import com.marianaalra.booklog.data.local.entity.BookEntity
import com.marianaalra.booklog.data.local.entity.ColeccionEntity
import com.marianaalra.booklog.data.local.entity.NoteEntiny // Usa tu nombre exacto
import com.marianaalra.booklog.data.local.entity.QuoteEntiny // Usa tu nombre exacto
import com.marianaalra.booklog.data.local.entity.SerieEntity
import com.marianaalra.booklog.data.local.entity.UserEntity
import com.marianaalra.booklog.data.remote.dto.BookDto
import com.marianaalra.booklog.data.remote.dto.ColeccionDto
import com.marianaalra.booklog.data.remote.dto.NoteDto
import com.marianaalra.booklog.data.remote.dto.QuoteDto
import com.marianaalra.booklog.data.remote.dto.SerieDto
import com.marianaalra.booklog.data.remote.dto.UserDto
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.model.ColeccionDomain
import com.marianaalra.booklog.domain.model.NoteDomain
import com.marianaalra.booklog.domain.model.QuoteDomain
import com.marianaalra.booklog.domain.model.SerieDomain
import com.marianaalra.booklog.domain.model.UserDomain

// ==================== ENTITY ↔ DOMAIN ====================

// --- TRADUCTORES DE LIBROS ---
fun BookEntity.toDomain(): Book = Book(
    id = id, usuarioId = usuarioId, fileUri = rutaArchivo, nombreArchivo = nombreArchivo,
    title = titulo, fileFormat = formato, author = autor, serieId = serieId,  progress = progreso, status = estado,coverPath = coverPath, fechaCreacion = fechaCreacion
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id, usuarioId = usuarioId, rutaArchivo = fileUri, nombreArchivo = nombreArchivo,
    titulo = title, formato = fileFormat, autor = author,  serieId = serieId, progreso = progress, estado = status,coverPath = coverPath, fechaCreacion = fechaCreacion
)

// --- TRADUCTORES DE NOTAS ---
fun NoteEntiny.toDomain(): NoteDomain = NoteDomain(
    id = id, bookId = lecturaId, contenido = contenido, referenciaPagina = referenciaPagina, fechaCreacion = fechaCreacion
)

fun NoteDomain.toEntity(): NoteEntiny = NoteEntiny(
    id = id, lecturaId = bookId, contenido = contenido, referenciaPagina = referenciaPagina, fechaCreacion = fechaCreacion
)

// --- TRADUCTORES DE CITAS ---
fun QuoteEntiny.toDomain(): QuoteDomain = QuoteDomain(
    id = id, bookId = lecturaId, textoCitado = textoCitado, comentario = comentario, referenciaPagina = referenciaPagina, fechaCreacion = fechaCreacion
)

fun QuoteDomain.toEntity(): QuoteEntiny = QuoteEntiny(
    id = id, lecturaId = bookId, textoCitado = textoCitado, comentario = comentario, referenciaPagina = referenciaPagina, fechaCreacion = fechaCreacion
)

// --- TRADUCTOR DE USUARIO (Solo hacia el Dominio, por seguridad no mapeamos contraseñas de vuelta) ---
fun UserEntity.toDomain(): UserDomain = UserDomain(id = id, nombreUsuario = nombreUsuario, correo = correo)

// --- SERIES ---
fun SerieEntity.toDomain() = SerieDomain(id = id, usuarioId = usuarioId, nombre = nombre)
fun SerieDomain.toEntity() = SerieEntity(id = id, usuarioId = usuarioId, nombre = nombre)

// --- COLECCIONES ---
fun ColeccionEntity.toDomain() = ColeccionDomain(id = id, usuarioId = usuarioId, nombre = nombre)
fun ColeccionDomain.toEntity() = ColeccionEntity(id = id, usuarioId = usuarioId, nombre = nombre)

// ==================== DTO REMOTO → DOMAIN ====================

// --- DTOs USUARIOS ---
fun UserDto.toDomain(): UserDomain = UserDomain(
    id = id,
    nombreUsuario = nombreUsuario,
    correo = correo
)

fun UserDto.toEntity(): UserEntity = UserEntity(
    id = id,
    nombreUsuario = nombreUsuario,
    correo = correo,
    hashContrasena = hashContrasena ?: "",
    fechaCreacion = fechaCreacion ?: System.currentTimeMillis()
)

// --- DTOs LIBROS ---
fun BookDto.toDomain(): Book = Book(
    id = id,
    usuarioId = usuarioId,
    title = titulo,
    fileFormat = formato,
    progress = progreso,
    status = estado,
    author = autor,
    serieId = serieId,
    fileUri = rutaArchivo ?: "",
    nombreArchivo = nombreArchivo,
    coverPath = coverPath,
    fechaCreacion = fechaCreacion ?: System.currentTimeMillis()
)

fun BookDto.toEntity(): BookEntity = BookEntity(
    id = id,
    usuarioId = usuarioId,
    rutaArchivo = rutaArchivo ?: "",
    nombreArchivo = nombreArchivo,
    titulo = titulo,
    formato = formato,
    autor = autor,
    serieId = serieId,
    progreso = progreso,
    estado = estado,
    coverPath = coverPath,
    fechaCreacion = fechaCreacion ?: System.currentTimeMillis()
)

// --- DTOs SERIES ---
fun SerieDto.toDomain(): SerieDomain = SerieDomain(
    id = id,
    usuarioId = usuarioId,
    nombre = nombre
)

fun SerieDto.toEntity(): SerieEntity = SerieEntity(
    id = id,
    usuarioId = usuarioId,
    nombre = nombre
)

// --- DTOs COLECCIONES ---
fun ColeccionDto.toDomain(): ColeccionDomain = ColeccionDomain(
    id = id,
    usuarioId = usuarioId,
    nombre = nombre
)

fun ColeccionDto.toEntity(): ColeccionEntity = ColeccionEntity(
    id = id,
    usuarioId = usuarioId,
    nombre = nombre
)

// --- DTOs NOTAS ---
fun NoteDto.toDomain(): NoteDomain = NoteDomain(
    id = id,
    bookId = lecturaId,
    contenido = contenido,
    referenciaPagina = referenciaPagina,
    fechaCreacion = fechaCreacion ?: System.currentTimeMillis()
)

fun NoteDto.toEntity(): NoteEntiny = NoteEntiny(
    id = id,
    lecturaId = lecturaId,
    contenido = contenido,
    referenciaPagina = referenciaPagina,
    fechaCreacion = fechaCreacion ?: System.currentTimeMillis()
)

// --- DTOs CITAS ---
fun QuoteDto.toDomain(): QuoteDomain = QuoteDomain(
    id = id,
    bookId = lecturaId,
    textoCitado = textoCitado,
    comentario = comentario,
    referenciaPagina = referenciaPagina,
    fechaCreacion = fechaCreacion ?: System.currentTimeMillis()
)

fun QuoteDto.toEntity(): QuoteEntiny = QuoteEntiny(
    id = id,
    lecturaId = lecturaId,
    textoCitado = textoCitado,
    comentario = comentario,
    referenciaPagina = referenciaPagina,
    fechaCreacion = fechaCreacion ?: System.currentTimeMillis()
)

