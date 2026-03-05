package com.marianaalra.booklog.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notas",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["lecturaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class NoteEntiny (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val lecturaId : Long,
    val contenido : String,
    val referenciaPagina : String?,
    val fechaCreacion: Long = System.currentTimeMillis()
)