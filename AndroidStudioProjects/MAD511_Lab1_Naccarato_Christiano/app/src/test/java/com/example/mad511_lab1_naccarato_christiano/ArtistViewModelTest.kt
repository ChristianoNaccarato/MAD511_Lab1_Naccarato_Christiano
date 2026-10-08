package com.example.mad511_lab1_naccarato_christiano

import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import com.example.mad511_lab1_naccarato_christiano.ui.ArtistViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ArtistViewModelTest {

    private lateinit var repository: ArtistRepository
    private lateinit var viewModel: ArtistViewModel

    @Before
    fun setUp() {
        repository = ArtistRepository.getInstance()
        viewModel = ArtistViewModel()
    }

    @Test
    fun yearValidationRule_outOfRangeYearDisablesAddButton_andSensibleYearEnablesIt() {
        viewModel.onNameChange("Drake")
        viewModel.onGenreChange("Hip-Hop")
        viewModel.onYearChange("-50")

        assertFalse(
            "Add button should be disabled when year is out of range",
            viewModel.isFormValid
        )

        viewModel.onYearChange("2006")

        assertTrue(
            "Add button should be enabled when year and all fields are valid",
            viewModel.isFormValid
        )
    }
}