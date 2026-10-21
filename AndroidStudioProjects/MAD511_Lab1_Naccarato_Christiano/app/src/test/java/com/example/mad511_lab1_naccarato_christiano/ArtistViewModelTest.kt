package com.example.mad511_lab1_naccarato_christiano

import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

fun isArtistInputValid(name: String, genre: String, yearString: String): Boolean {
    val year = yearString.toIntOrNull() ?: return false
    return name.isNotBlank() && genre.isNotBlank() && year in 1800..2026
}

class FakeArtistRepository : ArtistRepository {
    private val _artists = MutableStateFlow<List<Artist>>(emptyList())

    override fun observeAll(): Flow<List<Artist>> = _artists

    override fun observeById(id: Int): Flow<Artist?> = _artists.map { list ->
        list.find { it.id == id }
    }

    override fun search(query: String): Flow<List<Artist>> = _artists.map { list ->
        list.filter { it.name.contains(query, ignoreCase = true) || it.genre.contains(query, ignoreCase = true) }
    }

    override suspend fun addArtist(artist: Artist) {
        _artists.value = _artists.value + artist
    }

    override suspend fun updateArtist(artist: Artist) {
        _artists.value = _artists.value.map { if (it.id == artist.id) artist else it }
    }

    override suspend fun deleteArtist(artist: Artist) {
        _artists.value = _artists.value.filter { it.id != artist.id }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeArtistRepository
    private lateinit var viewModel: ArtistViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeArtistRepository()
        viewModel = ArtistViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun yearValidationRule_outOfRangeYearDisablesAddButton_andSensibleYearEnablesIt() {
        val name = "Drake"
        val genre = "Hip-Hop"
        var yearInput = "-50"

        assertFalse(
            "Add button should be disabled when year is out of range",
            isArtistInputValid(name, genre, yearInput)
        )

        yearInput = "2006"

        assertTrue(
            "Add button should be enabled when year and all fields are valid",
            isArtistInputValid(name, genre, yearInput)
        )
    }

    @Test
    fun addArtist_updatesArtistListInViewModel() = runTest {
        backgroundScope.launch(testDispatcher) {
            viewModel.uiState.collect()
        }

        val testArtist = Artist(id = 1, name = "Drake", genre = "Hip-Hop", yearFormed = 2006)

        viewModel.addArtist(testArtist)
        testDispatcher.scheduler.advanceUntilIdle()

        val list = viewModel.uiState.value.artists
        assertEquals(1, list.size)
        assertEquals("Drake", list[0].name)
    }
}