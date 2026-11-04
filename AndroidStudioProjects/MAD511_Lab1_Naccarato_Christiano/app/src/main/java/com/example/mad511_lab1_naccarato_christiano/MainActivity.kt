package com.example.mad511_lab1_naccarato_christiano

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.compose.ui.Modifier
import com.example.mad511_lab1_naccarato_christiano.ui.AddArtistScreen
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistDetailRoute
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistDetailViewModel
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistViewModel
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val backStack = rememberNavBackStack(SetListKey.ArtistList)

                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        entryProvider = entryProvider {
                            entry<SetListKey.ArtistList> {
                                ArtistRoute(
                                    onNavigateToAdd = { backStack.add(SetListKey.AddArtist) },
                                    onArtistClick = { id -> backStack.add(SetListKey.ArtistDetail(id)) }
                                )
                            }

                            entry<SetListKey.AddArtist> {
                                val vm: ArtistViewModel = hiltViewModel()
                                AddArtistScreen(
                                    viewModel = vm,
                                    onNavigateBack = { backStack.removeLastOrNull() }
                                )
                            }

                            entry<SetListKey.ArtistDetail> { key ->
                                val detailVm =
                                    hiltViewModel<ArtistDetailViewModel, ArtistDetailViewModel.Factory>(
                                        creationCallback = { factory -> factory.create(key.artistId) }
                                    )
                                val listVm: ArtistViewModel = hiltViewModel()
                                val uiState by listVm.uiState.collectAsStateWithLifecycle()

                                ArtistDetailRoute(
                                    viewModel = detailVm,
                                    onBack = { backStack.removeLastOrNull() },
                                    onDeleteClick = { id ->
                                        uiState.artists.find { it.id == id }
                                            ?.let { listVm.deleteArtist(it) }
                                        backStack.removeLastOrNull()
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}