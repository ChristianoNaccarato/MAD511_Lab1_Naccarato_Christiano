package com.example.mad511_lab1_naccarato_christiano.ui

import com.example.mad511_lab1_naccarato_christiano.data.Artist

data class ArtistListUiState(
    val artists: List<Artist> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
) {
    val isSearching: Boolean get() = searchQuery.isNotBlank()
}