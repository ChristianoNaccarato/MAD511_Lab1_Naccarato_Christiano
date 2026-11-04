package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ArtistDetailViewModel.Factory::class)
class ArtistDetailViewModel @AssistedInject constructor(
    @Assisted private val artistId: Int,
    private val repository: ArtistRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(artistId: Int): ArtistDetailViewModel
    }

    val artist: StateFlow<Artist?> = repository.observeById(artistId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun toggleFavorite() {
        val currentArtist = artist.value ?: return
        viewModelScope.launch {
            repository.updateArtist(currentArtist.copy(isFavorite = !currentArtist.isFavorite))
        }
    }
}