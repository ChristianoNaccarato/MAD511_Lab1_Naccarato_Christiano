package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import java.util.Calendar

class ArtistViewModel : ViewModel() {

    private val repository = ArtistRepository.getInstance()

    val artistList = mutableStateListOf<Artist>()

    // Form State
    var nameInput by mutableStateOf("")
        private set

    var genreInput by mutableStateOf("")
        private set

    var yearInput by mutableStateOf("")
        private set

    val isFormValid: Boolean
        get() {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val parsedYear = yearInput.toIntOrNull()
            return nameInput.isNotBlank() &&
                    genreInput.isNotBlank() &&
                    parsedYear != null &&
                    parsedYear in 1800..currentYear
        }

    init {
        refreshList()
    }

    fun onNameChange(newName: String) {
        nameInput = newName
    }

    fun onGenreChange(newGenre: String) {
        genreInput = newGenre
    }

    fun onYearChange(newYear: String) {
        yearInput = newYear
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