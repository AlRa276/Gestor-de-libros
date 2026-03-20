package com.marianaalra.booklog.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marianaalra.booklog.data.local.dao.BookDao
import com.marianaalra.booklog.data.local.dao.ColeccionDao
import com.marianaalra.booklog.data.local.dao.NoteDao
import com.marianaalra.booklog.data.local.dao.QuoteDao
import com.marianaalra.booklog.data.local.dao.SerieDao
import com.marianaalra.booklog.data.local.dao.UserDao
import com.marianaalra.booklog.data.local.entity.BookEntity
import com.marianaalra.booklog.data.local.entity.NoteEntiny
import com.marianaalra.booklog.data.local.entity.QuoteEntiny
import com.marianaalra.booklog.data.local.entity.UserEntity
import com.marianaalra.booklog.data.local.entity.SerieEntity
import com.marianaalra.booklog.data.local.entity.ColeccionEntity
import com.marianaalra.booklog.data.local.entity.LecturaColeccionEntity
// 1. Aquí declaramos TODAS nuestras entidades (tablas)
@Database(
    entities = [
        UserEntity::class,
        BookEntity::class,
        NoteEntiny::class,
        QuoteEntiny::class,
        SerieEntity::class,          // 👈 NUEVO
        ColeccionEntity::class,      // 👈 NUEVO
        LecturaColeccionEntity::class // 👈 NUEVO
    ],
    version = 4,   // 👈 sube a 4
    exportSchema = false
)
abstract class BookLogDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
    abstract fun noteDao(): NoteDao
    abstract fun quoteDao(): QuoteDao
    abstract fun serieDao(): SerieDao           // 👈 NUEVO
    abstract fun coleccionDao(): ColeccionDao   // 👈 NUEVO
}