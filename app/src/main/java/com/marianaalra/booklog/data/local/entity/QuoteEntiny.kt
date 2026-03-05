package com.marianaalra.booklog.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "citas",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["lecturaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuoteEntiny (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val lecturaId : Long,
    val textoCitado : String,
    val referenciaPagina : String?,
    val comentario : String?,
    val fechaCreacion: Long = System.currentTimeMillis()
)