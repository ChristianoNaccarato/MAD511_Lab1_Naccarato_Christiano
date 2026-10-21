package com.example.mad511_lab1_naccarato_christiano.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artists ORDER BY added_at DESC")
    fun observeAll(): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE id = :id")
    fun observeById(id: Int): Flow<ArtistEntity?>

    @Query("SELECT * FROM artists WHERE name LIKE :term || '%' ORDER BY name ASC")
    fun search(term: String): Flow<List<ArtistEntity>>

    @Upsert
    suspend fun upsert(artist: ArtistEntity)

    @Delete
    suspend fun delete(artist: ArtistEntity)
}