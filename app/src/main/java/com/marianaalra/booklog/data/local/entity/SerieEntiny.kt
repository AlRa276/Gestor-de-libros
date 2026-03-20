package com.marianaalra.booklog.data.local.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "series",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["usuarioId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SerieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: Long,
    val nombre: String,
    val fechaCreacion: Long = System.currentTimeMillis()
)