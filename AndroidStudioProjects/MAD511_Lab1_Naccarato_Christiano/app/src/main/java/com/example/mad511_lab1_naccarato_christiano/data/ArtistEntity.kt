package com.example.mad511_lab1_naccarato_christiano.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "artists_room3")
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val genre: String,
    val yearFormed: Int,
    val addedAt: Long = System.currentTimeMillis()
)

fun ArtistEntity.toDomain(): Artist {
    return Artist(
        id = this.id,
        name = this.name,
        genre = this.genre,
        yearFormed = this.yearFormed
    )
}

fun Artist.toEntity(): ArtistEntity {
    return ArtistEntity(
        id = this.id,
        name = this.name,
        genre = this.genre,
        yearFormed = this.yearFormed,
        addedAt = System.currentTimeMillis()
    )
}