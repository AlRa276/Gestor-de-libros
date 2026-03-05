package com.marianaalra.booklog.data.mapper

import com.marianaalra.booklog.data.local.entity.BookEntity
import com.marianaalra.booklog.data.local.entity.NoteEntiny // Usa tu nombre exacto
import com.marianaalra.booklog.data.local.entity.QuoteEntiny // Usa tu nombre exacto
import com.marianaalra.booklog.data.local.entity.UserEntity
import com.marianaalra.booklog.domain.model.Book
import com.marianaalra.booklog.domain.model.NoteDomain
import com.marianaalra.booklog.domain.model.QuoteDomain
import com.marianaalra.booklog.domain.model.UserDomain

// --- TRADUCTORES DE LIBROS ---
fun BookEntity.toDomain(): Book = Book(
    id = id, usuarioId = usuarioId, fileUri = rutaArchivo, nombreArchivo = nombreArchivo,
    title = titulo, fileFormat = formato, author = autor, progress = progreso, status = estado, fechaCreacion = fechaCreacion
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id, usuarioId = usuarioId, rutaArchivo = fileUri, nombreArchivo = nombreArchivo,
    titulo = title, formato = fileFormat, autor = author, progreso = progress, estado = status, fechaCreacion = fechaCreacion
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