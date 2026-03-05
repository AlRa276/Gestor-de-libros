package com.marianaalra.booklog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.marianaalra.booklog.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    // Usamos Flow para que tu pantalla se actualice sola si agregas o borras un libro
    @Query("SELECT * FROM lecturas WHERE usuarioId = :userId ORDER BY fechaCreacion DESC")
    fun getAllBooksForUser(userId: Long): Flow<List<BookEntity>>

    @Query("SELECT * FROM lecturas WHERE id = :bookId")
    suspend fun getBookById(bookId: Long): BookEntity?
}