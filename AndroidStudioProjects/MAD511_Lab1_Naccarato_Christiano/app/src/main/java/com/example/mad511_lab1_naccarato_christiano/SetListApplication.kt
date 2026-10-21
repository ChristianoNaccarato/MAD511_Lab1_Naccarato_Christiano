package com.example.mad511_lab1_naccarato_christiano

import android.app.Application
import com.example.mad511_lab1_naccarato_christiano.data.AppDatabase
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import com.example.mad511_lab1_naccarato_christiano.data.RoomArtistRepository

class SetListApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val repository: ArtistRepository by lazy { RoomArtistRepository(database.artistDao()) }
}