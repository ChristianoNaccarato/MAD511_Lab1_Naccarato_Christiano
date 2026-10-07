package com.example.mad511_lab1_naccarato_christiano

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

interface ArtistRepository {
    val artists: Flow<List<Artist>>
    fun getArtist(id: Int): Flow<Artist?>
    suspend fun addArtist(name: String, genre: String, yearFormed: Int)
    suspend fun deleteArtist(id: Int)
}

class InMemoryArtistRepository : ArtistRepository {
    private var nextId = 1
    private val _artists = MutableStateFlow<List<Artist>>(
        listOf(
            Artist(id = nextId++, name = "Drake", genre = "Hip-Hop", yearFormed = 2006),
            Artist(id = nextId++, name = "Future", genre = "Hip-Hop", yearFormed = 2003),
            Artist(id = nextId++, name = "Lil Wayne", genre = "Hip-Hop", yearFormed = 1996)
        )
    )

    override val artists: Flow<List<Artist>> = _artists.asStateFlow()

    override fun getArtist(id: Int): Flow<Artist?> {
        return _artists.map { list -> list.find { it.id == id } }
    }

    override suspend fun addArtist(name: String, genre: String, yearFormed: Int) {
        _artists.update { list ->
            list + Artist(id = nextId++, name = name, genre = genre, yearFormed = yearFormed)
        }
    }

    override suspend fun deleteArtist(id: Int) {
        _artists.update { list -> list.filterNot { it.id == id } }
    }
}
