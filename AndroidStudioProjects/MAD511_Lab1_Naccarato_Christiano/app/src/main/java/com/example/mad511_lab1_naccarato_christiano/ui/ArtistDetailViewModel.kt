package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mad511_lab1_naccarato_christiano.SetListApplication
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ArtistDetailViewModel(
    private val artistId: Int,
    private val repository: ArtistRepository
) : ViewModel() {

    val artist: StateFlow<Artist?> = repository.observeById(artistId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    companion object {
        fun Factory(artistId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SetListApplication
                ArtistDetailViewModel(artistId, app.repository)
            }
        }
    }
}