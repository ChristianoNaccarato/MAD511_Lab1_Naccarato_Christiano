package com.example.mad511_lab1_naccarato_christiano

import android.app.Application
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository

class SetListApplication : Application() {
    val repository: ArtistRepository by lazy {
        ArtistRepository.getInstance()
    }
}