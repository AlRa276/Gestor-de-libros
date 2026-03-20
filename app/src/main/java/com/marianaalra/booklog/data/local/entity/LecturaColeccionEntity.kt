package com.marianaalra.booklog.data.local.entity


import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "lecturas_colecciones",
    primaryKeys = ["lecturaId", "coleccionId"],
    foreignKeys = [
        ForeignKey(entity = BookEntity::class, parentColumns = ["id"], childColumns = ["lecturaId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ColeccionEntity::class, parentColumns = ["id"], childColumns = ["coleccionId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class LecturaColeccionEntity(
    val lecturaId: Long,
    val coleccionId: Long,
    val fechaAgregado: Long = System.currentTimeMillis()
)