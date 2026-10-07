package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mad511_lab1_naccarato_christiano.data.Artist

@Composable
fun ArtistDetailRoute(
    viewModel: ArtistDetailViewModel,
    onBack: () -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArtistDetailScreen(
        state = uiState,
        onBack = onBack,
        onDeleteClick = onDeleteClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    state: ArtistDetailUiState,
    onBack: () -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artist Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (state) {
                is ArtistDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ArtistDetailUiState.NotFound -> {
                    Text(
                        text = "Artist not found.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ArtistDetailUiState.Success -> {
                    val artist = state.artist
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = artist.name, style = MaterialTheme.typography.headlineMedium)
                        Text(text = "Genre: ${artist.genre}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Formed: ${artist.yearFormed}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onDeleteClick(artist.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete Artist")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistDetailScreenPreview() {
    ArtistDetailScreen(
        state = ArtistDetailUiState.Success(
            artist = Artist(id = 1, name = "Drake", genre = "Hip-Hop", yearFormed = 2006)
        ),
        onBack = {},
        onDeleteClick = {}
    )
}