package com.example.mad511_lab1_naccarato_christiano.data

import java.time.Instant
data class Artist(
    val id: Int = 0,
    val name: String,
    val genre: String,
    val yearFormed: Int,
    val addedAt: Instant = Instant.now(),
    val isFavorite: Boolean = false
)