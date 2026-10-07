package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mad511_lab1_naccarato_christiano.data.Artist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistListScreen(
    viewModel: ArtistViewModel,
    onNavigateToAdd: () -> Unit,
    onArtistClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Artist Collection") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add Artist")
            }
        }
    ) { innerPadding ->
        if (viewModel.artistList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No artists added yet",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = viewModel.artistList,
                    key = { it.id }
                ) { artist ->
                    ArtistRow(
                        artist = artist,
                        onClick = { onArtistClick(artist.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistRow(
    artist: Artist,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = artist.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${artist.genre} - ${artist.yearFormed}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
@Composable
fun ArtistStatelessContent(
    nameInput: String,
    onNameChange: (String) -> Unit,
    genreInput: String,
    onGenreChange: (String) -> Unit,
    yearInput: String,
    onYearChange: (String) -> Unit,
    isFormValid: Boolean,
    artistList: List<Artist>,
    onAddArtist: () -> Unit,
    onDeleteArtist: (Artist) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = nameInput,
            onValueChange = onNameChange,
            label = { Text("Artist Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = genreInput,
            onValueChange = onGenreChange,
            label = { Text("Genre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = yearInput,
            onValueChange = onYearChange,
            label = { Text("Year Formed") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onAddArtist,
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Artist")
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = artistList,
                key = { it.id }
            ) { artist ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = artist.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "${artist.genre} - ${artist.yearFormed}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Button(onClick = { onDeleteArtist(artist) }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistStatelessContentPreview() {
    MaterialTheme {
        ArtistStatelessContent(
            nameInput = "Drake",
            onNameChange = {},
            genreInput = "Hip-Hop",
            onGenreChange = {},
            yearInput = "2006",
            onYearChange = {},
            isFormValid = true,
            artistList = listOf(
                Artist(
                    id = 1,
                    name = "Drake",
                    genre = "Hip-Hop",
                    yearFormed = 2006
                ),
                Artist(
                    id = 2,
                    name = "Future",
                    genre = "Hip-Hop",
                    yearFormed = 2003
                )
            ),
            onAddArtist = {},
            onDeleteArtist = {}
        )
    }
}