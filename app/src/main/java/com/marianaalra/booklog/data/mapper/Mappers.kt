package com.marianaalra.booklog.data.mapper

import com.marianaalra.booklog.data.local.entity.BookEntity
import com.marianaalra.booklog.data.local.entity.ColeccionEntity
import com.marianaalra.booklog.data.local.entity.NoteEntiny // Usa tu nombre exacto
import com.marianaalra.booklog.data.local.entity.QuoteEntiny // Usa tu nombre exacto
import com.marianaalra.booklog.data.local.entity.SerieEntity
import com.marianaalra.booklog.data.local.entity.UserEntity
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.model.ColeccionDomain
import com.marianaalra.booklog.domain.model.NoteDomain
import com.marianaalra.booklog.domain.model.QuoteDomain
import com.marianaalra.booklog.domain.model.SerieDomain
import com.marianaalra.booklog.domain.model.UserDomain

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