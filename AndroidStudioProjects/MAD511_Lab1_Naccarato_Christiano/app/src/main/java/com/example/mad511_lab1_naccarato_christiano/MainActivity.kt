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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
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
                    // 1. Back stack list that survives orientation/rotation changes
                    val navBackStack = rememberSaveable(
                        saver = listSaver(
                            save = { it.toList() },
                            restore = { it.toMutableStateList() }
                        )
                    ) {
                        mutableStateListOf("list")
                    }

                    // 2. Remember selected artist ID across rotation
                    var selectedArtistId by rememberSaveable { mutableIntStateOf(-1) }

                    // 3. Render current top of back stack
                    when (navBackStack.lastOrNull() ?: "list") {
                        "list" -> ArtistListScreen(
                            viewModel = viewModel,
                            onNavigateToAdd = { navBackStack.add("add") },
                            onArtistClick = { id ->
                                selectedArtistId = id
                                navBackStack.add("detail")
                            }
                        )

                        "add" -> AddArtistScreen(
                            viewModel = viewModel,
                            onNavigateBack = {
                                if (navBackStack.size > 1) {
                                    navBackStack.removeAt(navBackStack.lastIndex)
                                }
                            }
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
                                    if (navBackStack.size > 1) {
                                        navBackStack.removeAt(navBackStack.lastIndex)
                                    }
                                },
                                onDeleteClick = { id ->
                                    val artistToDelete = viewModel.artistList.find { it.id == id }
                                    if (artistToDelete != null) {
                                        viewModel.deleteArtist(artistToDelete)
                                    }
                                    viewModel.refreshList()
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