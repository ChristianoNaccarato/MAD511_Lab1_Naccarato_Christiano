package com.example.mad511_lab1_naccarato_christiano.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mad511_lab1_naccarato_christiano.data.AppDatabase
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import com.example.mad511_lab1_naccarato_christiano.data.RoomArtistRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val ArtistViewModelFactory = viewModelFactory {
    initializer {
        val app = this[APPLICATION_KEY]!!
        val database = AppDatabase.getDatabase(app)
        val repository = RoomArtistRepository(database.artistDao())
        ArtistViewModel(repository)
    }
}

class ArtistViewModel(private val repository: ArtistRepository) : ViewModel() {

    val artistList: StateFlow<List<Artist>> = repository.observeAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun onQueryChange(value: String) {
        _query.value = value
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Artist>> = _query
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { term ->
            if (term.isBlank()) repository.observeAll() else repository.search(term)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addArtist(artist: Artist) = viewModelScope.launch {
        repository.addArtist(artist)
    }

    fun updateArtist(artist: Artist) = viewModelScope.launch {
        repository.updateArtist(artist)
    }

    fun deleteArtist(artist: Artist) = viewModelScope.launch {
        repository.deleteArtist(artist)
    }
}