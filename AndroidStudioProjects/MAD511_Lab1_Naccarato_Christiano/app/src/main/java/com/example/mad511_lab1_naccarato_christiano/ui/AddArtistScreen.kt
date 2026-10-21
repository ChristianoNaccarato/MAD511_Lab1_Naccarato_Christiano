package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mad511_lab1_naccarato_christiano.data.Artist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArtistScreen(
    viewModel: ArtistViewModel,
    onNavigateBack: () -> Unit
) {
    var nameInput by rememberSaveable { mutableStateOf("") }
    var genreInput by rememberSaveable { mutableStateOf("") }
    var yearInput by rememberSaveable { mutableStateOf("") }

    val isFormValid = nameInput.isNotBlank() && genreInput.isNotBlank() && yearInput.toIntOrNull() != null

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add New Artist") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text("Artist Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = genreInput,
                onValueChange = { genreInput = it },
                label = { Text("Genre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = yearInput,
                onValueChange = { yearInput = it },
                label = { Text("Year Formed") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val year = yearInput.toIntOrNull() ?: 0
                    val newArtist = Artist(
                        id = 0,
                        name = nameInput.trim(),
                        genre = genreInput.trim(),
                        yearFormed = year
                    )
                    viewModel.addArtist(newArtist)
                    onNavigateBack()
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Artist")
            }
        }
    }
}