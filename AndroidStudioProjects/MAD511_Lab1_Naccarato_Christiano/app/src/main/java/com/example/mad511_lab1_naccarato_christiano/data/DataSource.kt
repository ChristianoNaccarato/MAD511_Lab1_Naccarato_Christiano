package com.example.mad511_lab1_naccarato_christiano.data

class DataSource {
    private val artists = mutableListOf<Artist>()
    private var nextId = 1

    fun getArtists(): List<Artist> {
        return artists.toList()
    }

    fun addArtist(name: String, genre: String, yearFormed: Int) {
        val newArtist = Artist(
            id = nextId++,
            name = name,
            genre = genre,
            yearFormed = yearFormed
        )
        artists.add(newArtist)
    }

    fun deleteArtist(artist: Artist) {
        artists.removeAll { it.id == artist.id }
    }
}