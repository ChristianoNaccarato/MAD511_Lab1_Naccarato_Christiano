package com.example.mad511_lab1_naccarato_christiano.data

import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun observeAll(): Flow<List<Artist>>
    fun observeById(id: Int): Flow<Artist?>
    fun search(query: String): Flow<List<Artist>>
    suspend fun addArtist(artist: Artist)
    suspend fun updateArtist(artist: Artist)
    suspend fun deleteArtist(artist: Artist)
}