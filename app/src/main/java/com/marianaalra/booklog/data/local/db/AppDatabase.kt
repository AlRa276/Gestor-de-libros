// data/local/db/AppDatabase.kt
package com.marianaalra.booklog.data.local.db

import android.content.Context
import androidx.room.Room

object AppDatabase {
    @Volatile
    private var INSTANCE: BookLogDatabase? = null

    fun getInstance(context: Context): BookLogDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                BookLogDatabase::class.java,
                "booklog_database"
            ).build().also { INSTANCE = it }
        }
    }
}