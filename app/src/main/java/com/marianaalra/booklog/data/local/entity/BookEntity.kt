package com.marianaalra.booklog.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Relacionamos los libros con la tabla de usuarios
@Entity(
    tableName = "lecturas",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE // Si borras al usuario, se borran sus libros
        )
    ]
)
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: Long,
    val rutaArchivo: String,
    val nombreArchivo: String,
    val titulo: String,
    val formato: String,
    val autor: String?,
    val serieId: Long? = null,
    val progreso: Float = 0f, // 👈 El que agregamos para tu Slider
    val estado: String = "PENDIENTE",
    val coverPath: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)