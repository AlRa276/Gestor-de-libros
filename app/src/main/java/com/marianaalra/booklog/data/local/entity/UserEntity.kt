package com.marianaalra.booklog.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombreUsuario: String,
    val correo: String,
    val hashContrasena: String, // 👈 Aquí SÍ guardamos contraseñas, en el dominio NO.
    val fechaCreacion: Long = System.currentTimeMillis()
)