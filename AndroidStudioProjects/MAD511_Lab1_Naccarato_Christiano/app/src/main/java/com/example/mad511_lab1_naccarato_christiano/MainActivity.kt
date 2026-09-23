package com.example.mad511_lab1_naccarato_christiano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.collections.remove

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtistAppScreen()
                }
            }
        }
    }
}

@Composable
fun ArtistAppScreen() {
    // The Add Artist form state setup
    var nameInput by remember { mutableStateOf("") }
    var genreInput by remember { mutableStateOf("") }
    var yearInput by remember { mutableStateOf("") }

    val artistList = remember { mutableStateListOf<Artist>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Form Inputs
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Artist Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = genreInput,
            onValueChange = { genreInput = it },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = yearInput,
            onValueChange = { yearInput = it },
            label = { Text("Year Formed") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Add Button
        Button(
            onClick = {
                val year = yearInput.toIntOrNull()
                if (year != null) {
                    artistList.add(
                        Artist(
                            name = nameInput,
                            genre = genreInput,
                            yearFormed = year
                        )
                    )
                    nameInput = ""
                    genreInput = ""
                    yearInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // The artist list display using LazyColumn
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(artistList) { artist ->
                ArtistRow(
                    artist = artist,
                    onDelete = { artistList.remove(artist) }
                )
                Button(
                    onClick = {
                        artistList.remove( artist)
                    }
                ) { Text("Delete")}

            }
        }
    }
}

@Composable
fun ArtistRow(
    artist: Artist,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${artist.genre} - ${artist.yearFormed}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}