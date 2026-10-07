package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository

class ArtistViewModel : ViewModel() {

    private val repository = ArtistRepository.getInstance()

    val artistList = mutableStateListOf<Artist>()

    init {
        refreshList()
    }

    fun refreshList() {
        artistList.clear()
        artistList.addAll(repository.getArtists())
    }

    fun addArtist(name: String, genre: String, yearFormed: Int) {
        repository.addArtist(name, genre, yearFormed)
        refreshList()
    }

    fun deleteArtist(artist: Artist) {
        repository.deleteArtist(artist)
        refreshList()
    }
}