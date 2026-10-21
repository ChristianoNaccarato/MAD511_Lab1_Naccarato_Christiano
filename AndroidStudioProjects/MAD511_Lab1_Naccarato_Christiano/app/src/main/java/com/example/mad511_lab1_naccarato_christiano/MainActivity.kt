package com.example.mad511_lab1_naccarato_christiano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mad511_lab1_naccarato_christiano.ui.AddArtistScreen
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistDetailRoute
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistDetailViewModel
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistListScreen
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistViewModel
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mainViewModel: ArtistViewModel = viewModel(factory = ArtistViewModelFactory.Factory)

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navBackStack = rememberSaveable(
                        saver = listSaver(
                            save = { it.toList() },
                            restore = { it.toMutableStateList() }
                        )
                    ) {
                        mutableStateListOf("list")
                    }

                    var selectedArtistId by rememberSaveable { mutableIntStateOf(-1) }

                    when (navBackStack.lastOrNull() ?: "list") {
                        "list" -> ArtistListScreen(
                            viewModel = mainViewModel,
                            onNavigateToAdd = { navBackStack.add("add") },
                            onArtistClick = { id ->
                                selectedArtistId = id
                                navBackStack.add("detail")
                            }
                        )

                        "add" -> AddArtistScreen(
                            viewModel = mainViewModel,
                            onNavigateBack = {
                                if (navBackStack.size > 1) {
                                    navBackStack.removeAt(navBackStack.lastIndex)
                                }
                            }
                        )

                        "detail" -> {
                            val detailViewModel: ArtistDetailViewModel = viewModel(
                                key = selectedArtistId.toString(),
                                factory = ArtistDetailViewModel.Factory(selectedArtistId)
                            )

                            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

                            ArtistDetailRoute(
                                viewModel = detailViewModel,
                                onBack = {
                                    if (navBackStack.size > 1) {
                                        navBackStack.removeAt(navBackStack.lastIndex)
                                    }
                                },
                                onDeleteClick = { id ->
                                    val artistToDelete = uiState.artists.find { it.id == id }
                                    if (artistToDelete != null) {
                                        mainViewModel.deleteArtist(artistToDelete)
                                    }
                                    if (navBackStack.size > 1) {
                                        navBackStack.removeAt(navBackStack.lastIndex)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}