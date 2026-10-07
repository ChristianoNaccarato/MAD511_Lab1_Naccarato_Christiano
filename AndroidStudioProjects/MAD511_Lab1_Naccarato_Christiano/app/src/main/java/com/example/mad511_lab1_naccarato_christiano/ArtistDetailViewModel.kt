package com.example.mad511_lab1_naccarato_christiano

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface ArtistDetailUiState {
    object Loading : ArtistDetailUiState
    object NotFound : ArtistDetailUiState
    data class Success(val artist: Artist) : ArtistDetailUiState
}

class ArtistDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ArtistDetailUiState>(ArtistDetailUiState.Loading)
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()
}