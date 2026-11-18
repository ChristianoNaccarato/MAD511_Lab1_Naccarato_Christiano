package com.example.mad511_lab1_naccarato_christiano.ui

import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.work.BackupStatus

data class ArtistListUiState(
    val artists: List<Artist> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val backupStatus: BackupStatus = BackupStatus.NothingRun
) {
    val isSearching: Boolean get() = searchQuery.isNotBlank()
}