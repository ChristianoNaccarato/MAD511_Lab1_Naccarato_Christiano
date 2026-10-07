package com.example.mad511_lab1_naccarato_christiano.data

class ArtistRepository {
    private val dataSource = DataSource()

    fun getArtists(): List<Artist> {
        return dataSource.getArtists()
    }

    fun addArtist(name: String, genre: String, yearFormed: Int) {
        dataSource.addArtist(name, genre, yearFormed)
    }

    fun deleteArtist(artist: Artist) {
        dataSource.deleteArtist(artist)
    }
}