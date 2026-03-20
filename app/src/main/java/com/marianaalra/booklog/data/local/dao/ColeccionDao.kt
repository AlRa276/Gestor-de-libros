package com.marianaalra.booklog.data.local.dao

import androidx.room.*
import com.marianaalra.booklog.data.local.entity.BookEntity
import com.marianaalra.booklog.data.local.entity.ColeccionEntity
import com.marianaalra.booklog.data.local.entity.LecturaColeccionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ColeccionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColeccion(coleccion: ColeccionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLecturaColeccion(relacion: LecturaColeccionEntity)

    @Query("SELECT * FROM colecciones WHERE usuarioId = :userId ORDER BY nombre ASC")
    fun getColeccionesForUser(userId: Long): Flow<List<ColeccionEntity>>

    @Query("SELECT * FROM colecciones WHERE usuarioId = :userId AND nombre LIKE '%' || :query || '%'")
    suspend fun searchColecciones(userId: Long, query: String): List<ColeccionEntity>

    @Query("SELECT c.* FROM colecciones c INNER JOIN lecturas_colecciones lc ON c.id = lc.coleccionId WHERE lc.lecturaId = :bookId")
    fun getColeccionesForBook(bookId: Long): Flow<List<ColeccionEntity>>

    @Query("DELETE FROM lecturas_colecciones WHERE lecturaId = :bookId")
    suspend fun deleteAllColeccionesForBook(bookId: Long)

    @Query("""
    SELECT lecturas.* FROM lecturas 
    INNER JOIN lecturas_colecciones ON lecturas.id = lecturas_colecciones.lecturaId 
    WHERE lecturas_colecciones.coleccionId = :coleccionId
""")
    fun getBooksForColeccion(coleccionId: Long): Flow<List<BookEntity>>

    @Delete
    suspend fun deleteLecturaColeccion(relacion: LecturaColeccionEntity)

    @Delete
    suspend fun deleteColeccion(coleccion: ColeccionEntity)
}