package com.marianaalra.book.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marianaalra.book.data.local.entity.NoteEntiny
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntiny)

    @Update
    suspend fun updateNote(note: NoteEntiny)

    @Delete
    suspend fun deleteNote(note: NoteEntiny)

    @Query("SELECT * FROM notas WHERE lecturaId = :bookId ORDER BY fechaCreacion DESC")
    fun getNotesForBook(bookId: Long): Flow<List<NoteEntiny>>
}