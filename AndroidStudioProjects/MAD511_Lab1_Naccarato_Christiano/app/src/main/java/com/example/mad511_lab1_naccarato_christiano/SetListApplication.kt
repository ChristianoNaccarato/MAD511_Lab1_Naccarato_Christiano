package com.example.mad511_lab1_naccarato_christiano

import android.app.Application

class AppContainer {
    val artistRepository: ArtistRepository by lazy {
        InMemoryArtistRepository()
    }
}

class SetListApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}