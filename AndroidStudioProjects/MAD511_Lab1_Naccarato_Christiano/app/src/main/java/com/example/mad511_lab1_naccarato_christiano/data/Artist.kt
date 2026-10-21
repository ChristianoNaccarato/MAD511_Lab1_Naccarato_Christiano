package com.example.mad511_lab1_naccarato_christiano.data

import java.time.Instant
data class Artist(
    val id: Int = 0,
    val name: String,
    val genre: String,
    val yearFormed: Int,
    val isFavorite: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)