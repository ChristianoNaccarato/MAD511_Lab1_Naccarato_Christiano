package com.example.mad511_lab1_naccarato_christiano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mad511_lab1_naccarato_christiano.ui.AddArtistScreen
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistListScreen
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ArtistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("list") }

                    when (currentScreen) {
                        "list" -> ArtistListScreen(
                            viewModel = viewModel,
                            onNavigateToAdd = { currentScreen = "add" }
                        )
                        "add" -> AddArtistScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "list" }
                        )
                    }
                }
            }
        }
    }
}