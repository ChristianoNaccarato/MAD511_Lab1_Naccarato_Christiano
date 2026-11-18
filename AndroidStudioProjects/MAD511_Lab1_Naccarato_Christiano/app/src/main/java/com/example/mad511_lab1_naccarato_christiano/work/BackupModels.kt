package com.example.mad511_lab1_naccarato_christiano.work

import com.example.mad511_lab1_naccarato_christiano.data.Artist
import kotlinx.serialization.Serializable

@Serializable
data class BackupArtist(
    val name: String,
    val genre: String,
    val yearFormed: Int,
    val isFavorite: Boolean
)

@Serializable
data class BackupFile(
    val createdAt: Long,
    val artists: List<BackupArtist>
)

fun Artist.toBackupArtist(): BackupArtist {
    return BackupArtist(
        name = this.name,
        genre = this.genre,
        yearFormed = this.yearFormed,
        isFavorite = this.isFavorite
    )
}