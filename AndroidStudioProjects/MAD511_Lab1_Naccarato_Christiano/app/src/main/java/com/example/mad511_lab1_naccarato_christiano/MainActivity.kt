package com.example.mad511_lab1_naccarato_christiano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import com.example.mad511_lab1_naccarato_christiano.ui.AddArtistScreen
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistDetailRoute
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistDetailViewModel
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
                    var selectedArtistId by remember { mutableIntStateOf(-1) }

                    when (currentScreen) {
                        "list" -> ArtistListScreen(
                            viewModel = viewModel,
                            onNavigateToAdd = { currentScreen = "add" },
                            onArtistClick = { id ->
                                selectedArtistId = id
                                currentScreen = "detail"
                            }
                        )

                        "add" -> AddArtistScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "list" }
                        )

                        "detail" -> {
                            val repository = ArtistRepository.getInstance()
                            val detailViewModel: ArtistDetailViewModel = viewModel(
                                key = selectedArtistId.toString(),
                                factory = ArtistDetailViewModel.Factory(selectedArtistId, repository)
                            )

                            ArtistDetailRoute(
                                viewModel = detailViewModel,
                                onBack = {
                                    viewModel.refreshList()
                                    currentScreen = "list"
                                },
                                onDeleteClick = { id ->
                                    val artistToDelete = viewModel.artistList.find { it.id == id }
                                    if (artistToDelete != null) {
                                        viewModel.deleteArtist(artistToDelete)
                                    }
                                    currentScreen = "list"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}