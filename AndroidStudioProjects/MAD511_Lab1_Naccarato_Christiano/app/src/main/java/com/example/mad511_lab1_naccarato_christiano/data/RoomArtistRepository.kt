package com.example.mad511_lab1_naccarato_christiano.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomArtistRepository(
    private val artistDao: ArtistDao
) : ArtistRepository {

    override fun observeAll(): Flow<List<Artist>> {
        return artistDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeById(id: Int): Flow<Artist?> {
        return artistDao.observeById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override fun search(query: String): Flow<List<Artist>> {
        return artistDao.search(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addArtist(artist: Artist) {
        artistDao.insert(artist.toEntity())
    }

    override suspend fun updateArtist(artist: Artist) {
        artistDao.update(artist.toEntity())
    }

    override suspend fun deleteArtist(artist: Artist) {
        artistDao.delete(artist.toEntity())
    }
}