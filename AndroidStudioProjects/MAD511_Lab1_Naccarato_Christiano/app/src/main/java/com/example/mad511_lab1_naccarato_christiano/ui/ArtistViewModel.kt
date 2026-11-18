package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import com.example.mad511_lab1_naccarato_christiano.work.BackupActions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ArtistViewModel @Inject constructor(
    private val repository: ArtistRepository,
    private val backup: BackupActions
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<ArtistListUiState> = _searchQuery
        .map { query ->
            ArtistListUiState(
                searchQuery = query,
                isLoading = true
            )
        }
        .combine(
            _searchQuery
                .debounce(300L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        repository.observeAll()
                    } else {
                        repository.search(query.trim())
                    }
                }
        ) { uiState, artistList ->
            uiState.copy(
                artists = artistList,
                isLoading = false
            )
        }
        .combine(backup.status()) { uiState, status ->
            uiState.copy(backupStatus = status)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArtistListUiState(isLoading = true)
        )

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun backupNow() {
        backup.backupNow()
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