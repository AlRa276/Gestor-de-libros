package com.marianaalra.booklog.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marianaalra.booklog.data.local.dao.BookDao
import com.marianaalra.booklog.data.local.dao.NoteDao
import com.marianaalra.booklog.data.local.dao.QuoteDao
import com.marianaalra.booklog.data.local.dao.UserDao
import com.marianaalra.booklog.data.local.entity.BookEntity
import com.marianaalra.booklog.data.local.entity.NoteEntiny
import com.marianaalra.booklog.data.local.entity.QuoteEntiny
import com.marianaalra.booklog.data.local.entity.UserEntity

// 1. Aquí declaramos TODAS nuestras entidades (tablas)
@Database(
    entities = [
        UserEntity::class,
        BookEntity::class,
        NoteEntiny::class,
        QuoteEntiny::class
    ],
    version = 2, // Si en el futuro agregas una columna, cambias este número a 2
    exportSchema = false
)
abstract class BookLogDatabase : RoomDatabase() {

    // 2. Aquí conectamos los DAOs
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
    abstract fun noteDao(): NoteDao
    abstract fun quoteDao(): QuoteDao

}