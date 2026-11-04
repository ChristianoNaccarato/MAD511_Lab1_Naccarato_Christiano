package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ArtistRoute(
    onNavigateToAdd: () -> Unit,
    onArtistClick: (Int) -> Unit,
    viewModel: ArtistViewModel = hiltViewModel()
) {
    ArtistListScreen(
        viewModel = viewModel,
        onNavigateToAdd = onNavigateToAdd,
        onArtistClick = onArtistClick
    )
}