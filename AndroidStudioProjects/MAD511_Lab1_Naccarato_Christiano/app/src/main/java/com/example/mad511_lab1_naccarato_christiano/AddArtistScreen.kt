package com.example.mad511_lab1_naccarato_christiano

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddArtistRoute(
    viewModel: AddArtistViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AddArtistScreen(
        state = uiState,
        onNameChange = viewModel::onNameChanged,
        onGenreChange = viewModel::onGenreChanged,
        onYearChange = viewModel::onYearChanged,
        onSave = { viewModel.saveArtist(onSaved = onNavigateBack) },
        onBack = onNavigateBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArtistScreen(
    state: AddArtistUiState,
    onNameChange: (String) -> Unit,
    onGenreChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Artist") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = { Text("Artist Name") },
                isError = state.nameError,
                supportingText = { if (state.nameError) Text("Name cannot be empty") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.genre,
                onValueChange = onGenreChange,
                label = { Text("Genre") },
                isError = state.genreError,
                supportingText = { if (state.genreError) Text("Genre cannot be empty") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.yearFormed,
                onValueChange = onYearChange,
                label = { Text("Year Formed") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.yearError,
                supportingText = {
                    if (state.yearError) Text("Year must be between 1800 and ${state.currentYear}")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onSave,
                enabled = state.isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Artist")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddArtistScreenPreview() {
    AddArtistScreen(
        state = AddArtistUiState(),
        onNameChange = {},
        onGenreChange = {},
        onYearChange = {},
        onSave = {},
        onBack = {}
    )
}