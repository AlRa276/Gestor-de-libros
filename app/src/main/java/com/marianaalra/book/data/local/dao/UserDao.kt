package com.marianaalra.book.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marianaalra.book.data.local.entity.UserEntity

@Dao
interface UserDao {
    // Inserta un usuario, si hay un conflicto, lo reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    // Busca un usuario por correo para el Login
    @Query("SELECT * FROM usuarios WHERE correo = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
}