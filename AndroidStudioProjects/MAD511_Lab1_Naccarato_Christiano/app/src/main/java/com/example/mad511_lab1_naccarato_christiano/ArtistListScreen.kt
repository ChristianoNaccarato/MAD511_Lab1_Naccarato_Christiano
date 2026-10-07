package com.example.mad511_lab1_naccarato_christiano

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ArtistListRoute(
    viewModel: ArtistListViewModel,
    onArtistClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArtistListScreen(
        state = uiState,
        onArtistClick = onArtistClick,
        onAddClick = onAddClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistListScreen(
    state: ArtistListUiState,
    onArtistClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("SetList - Artists") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Artist")
            }
        }
    ) { padding ->
        if (state.artists.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No artists found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(items = state.artists, key = { it.id }) { artist ->
                    ListItem(
                        headlineContent = { Text(artist.name) },
                        supportingContent = { Text("${artist.genre} • ${artist.yearFormed}") },
                        modifier = Modifier.clickable { onArtistClick(artist.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistListScreenPreview() {
    ArtistListScreen(
        state = ArtistListUiState(
            artists = listOf(
                Artist(id = 1, name = "Drake", genre = "Hip-Hop", yearFormed = 2006),
                Artist(id = 2, name = "Future", genre = "Hip-Hop", yearFormed = 2003),
                Artist(id = 3, name = "Lil Wayne", genre = "Hip-Hop", yearFormed = 1996)
            )
        ),
        onArtistClick = {},
        onAddClick = {}
    )
}