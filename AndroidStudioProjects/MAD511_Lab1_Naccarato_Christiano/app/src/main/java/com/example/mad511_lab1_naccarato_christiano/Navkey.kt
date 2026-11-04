package com.example.mad511_lab1_naccarato_christiano

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface SetListKey : NavKey {
    @Serializable
    data object ArtistList : SetListKey

    @Serializable
    data object AddArtist : SetListKey

    @Serializable
    data class ArtistDetail(val artistId: Int) : SetListKey

    @Serializable
    data class ConfirmDelete(val artistId: Int) : SetListKey
}