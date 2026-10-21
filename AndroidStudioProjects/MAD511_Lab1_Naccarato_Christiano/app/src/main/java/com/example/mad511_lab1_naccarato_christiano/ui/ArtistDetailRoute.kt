package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ArtistDetailRoute(
    viewModel: ArtistDetailViewModel,
    onBack: () -> Unit,
    onDeleteClick: (Int) -> Unit
) {

    val artist by viewModel.artist.collectAsStateWithLifecycle()

    ArtistDetailScreen(
        artist = artist,
        onBack = onBack,
        onDeleteClick = onDeleteClick
    )
}