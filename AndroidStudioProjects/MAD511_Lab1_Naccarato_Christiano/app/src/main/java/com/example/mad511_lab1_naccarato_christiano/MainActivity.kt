package com.example.mad511_lab1_naccarato_christiano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

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

//Stateful-owns the state passes values down and receives events back up
@Composable
fun ArtistAppScreen(
    modifier: Modifier = Modifier
) {
    var nameInput by remember { mutableStateOf("") }
    var genreInput by remember { mutableStateOf("") }
    var yearInput by remember { mutableStateOf("") }

    val artistList = remember { mutableStateListOf<Artist>() }

    ArtistStatelessContent(
        nameInput = nameInput,
        onNameChange = { nameInput = it },
        genreInput = genreInput,
        onGenreChange = { genreInput = it },
        yearInput = yearInput,
        onYearChange = { yearInput = it },
        artistList = artistList,
        onAddArtist = {
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
        onDeleteArtist = { artist ->
            artistList.remove(artist)
        },
        modifier = modifier
    )
}


// Stateless-every value arrives as a parameter, so it renders in the preview with no app running
@Composable
fun ArtistStatelessContent(
    nameInput: String,
    onNameChange: (String) -> Unit,
    genreInput: String,
    onGenreChange: (String) -> Unit,
    yearInput: String,
    onYearChange: (String) -> Unit,
    artistList: List<Artist>,
    onAddArtist: () -> Unit,
    onDeleteArtist: (Artist) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Uses the number keyboard
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onAddArtist,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Artist")
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(artistList) { artist ->
                    ArtistRow(
                        artist = artist,
                        onDelete = { onDeleteArtist(artist) }
                    )
                }
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
                    text = "${artist.genre} -${artist.yearFormed}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}
// Stateless form preview
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
            artistList = listOf(
                Artist(
                    name = "Drake",
                    genre = "Hip-Hop",
                    yearFormed = 2006
                ),
                Artist(
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