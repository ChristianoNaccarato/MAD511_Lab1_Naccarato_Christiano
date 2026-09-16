package com.example.mad511_lab1_naccarato_christiano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    }
}