package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface ArtistDetailUiState {
    data object Loading : ArtistDetailUiState
    data object NotFound : ArtistDetailUiState
    data class Success(val artist: Artist) : ArtistDetailUiState
}

class ArtistDetailViewModel(
    private val artistId: Int,
    private val repository: ArtistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArtistDetailUiState>(ArtistDetailUiState.Loading)
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()

    init {
        loadArtist()
    }

    private fun loadArtist() {
        val artist = repository.getArtists().find { it.id == artistId }
        _uiState.value = if (artist != null) {
            ArtistDetailUiState.Success(artist)
        } else {
            ArtistDetailUiState.NotFound
        }
    }

    class Factory(
        private val artistId: Int,
        private val repository: ArtistRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ArtistDetailViewModel(artistId, repository) as T
        }
    }
}