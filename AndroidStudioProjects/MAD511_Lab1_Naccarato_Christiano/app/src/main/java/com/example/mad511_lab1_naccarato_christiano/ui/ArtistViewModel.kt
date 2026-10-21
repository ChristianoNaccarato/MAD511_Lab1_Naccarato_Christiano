package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistViewModel(
    private val repository: ArtistRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<ArtistListUiState> = _searchQuery
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.observeAll()
            } else {
                repository.search(query.trim())
            }
        }
        .map { artistList ->
            ArtistListUiState(
                artists = artistList,
                searchQuery = _searchQuery.value
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArtistListUiState(isLoading = true)
        )

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun addArtist(artist: Artist) {
        viewModelScope.launch {
            repository.addArtist(artist)
        }
    }

    fun deleteArtist(artist: Artist) {
        viewModelScope.launch {
            repository.deleteArtist(artist)
        }
    }

    fun toggleFavorite(artist: Artist) {
        viewModelScope.launch {
            repository.updateArtist(artist.copy(isFavorite = !artist.isFavorite))
        }
    }
}