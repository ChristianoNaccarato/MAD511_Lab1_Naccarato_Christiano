package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ArtistRoute(
    onNavigateToAdd: () -> Unit,
    onArtistClick: (Int) -> Unit,
    viewModel: ArtistViewModel = viewModel(factory = ArtistViewModelFactory)
) {
    ArtistListScreen(
        viewModel = viewModel,
        onNavigateToAdd = onNavigateToAdd,
        onArtistClick = onArtistClick
    )
}