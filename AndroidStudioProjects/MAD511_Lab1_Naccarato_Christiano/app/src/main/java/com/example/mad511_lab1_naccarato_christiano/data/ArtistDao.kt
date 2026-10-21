package com.example.mad511_lab1_naccarato_christiano.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists_room3")
    fun observeAll(): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists_room3 WHERE id = :id")
    fun observeById(id: Int): Flow<ArtistEntity?>

    @Query("SELECT * FROM artists_room3 WHERE name LIKE '%' || :query || '%' OR genre LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<ArtistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: ArtistEntity)

    @Update
    suspend fun update(artist: ArtistEntity)

    @Delete
    suspend fun delete(artist: ArtistEntity)

    @Upsert
    suspend fun upsertArtist(artist: ArtistEntity)
}