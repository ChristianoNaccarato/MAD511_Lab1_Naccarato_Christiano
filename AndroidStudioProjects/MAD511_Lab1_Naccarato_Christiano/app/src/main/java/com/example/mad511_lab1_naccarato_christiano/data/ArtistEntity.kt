package com.example.mad511_lab1_naccarato_christiano.data

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import java.time.Instant

@Entity(tableName = "artists", indices = [Index(value = ["name"], unique = true)])
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val genre: String,
    @ColumnInfo(name = "year_formed") val yearFormed: Int,
    @ColumnInfo(name = "added_at") val addedAt: Instant
)

fun ArtistEntity.toDomainModel() = Artist(id, name, genre, yearFormed, addedAt)

fun Artist.toEntity() = ArtistEntity(id, name, genre, yearFormed, addedAt)