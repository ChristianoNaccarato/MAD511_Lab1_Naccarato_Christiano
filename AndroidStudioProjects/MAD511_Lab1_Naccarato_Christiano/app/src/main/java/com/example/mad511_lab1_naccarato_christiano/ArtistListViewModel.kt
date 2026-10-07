package com.example.mad511_lab1_naccarato_christiano

import androidx.lifecycle.ViewModel
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ArtistListUiState(
    val artists: List<Artist> = emptyList()
)

class ArtistListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ArtistListUiState())
    val uiState: StateFlow<ArtistListUiState> = _uiState.asStateFlow()
}