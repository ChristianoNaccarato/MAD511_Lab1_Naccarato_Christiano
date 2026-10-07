package com.example.mad511_lab1_naccarato_christiano

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AddArtistUiState(
    val name: String = "",
    val genre: String = "",
    val yearFormed: String = "",
    val nameError: Boolean = false,
    val genreError: Boolean = false,
    val yearError: Boolean = false,
    val currentYear: Int = 2026,
    val isValid: Boolean = false
)

class AddArtistViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddArtistUiState())
    val uiState: StateFlow<AddArtistUiState> = _uiState.asStateFlow()

    fun onNameChanged(name: String) {}
    fun onGenreChanged(genre: String) {}
    fun onYearChanged(year: String) {}
    fun saveArtist(onSaved: () -> Unit) {}
}