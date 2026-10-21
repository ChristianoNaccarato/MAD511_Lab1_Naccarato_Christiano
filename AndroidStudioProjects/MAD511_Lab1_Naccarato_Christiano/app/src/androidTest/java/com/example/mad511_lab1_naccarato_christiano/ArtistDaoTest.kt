package com.example.mad511_lab1_naccarato_christiano

import com.example.mad511_lab1_naccarato_christiano.data.ArtistDao
import com.example.mad511_lab1_naccarato_christiano.data.ArtistEntity
import com.example.mad511_lab1_naccarato_christiano.data.AppDatabase
import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ArtistDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var artistDao: ArtistDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setDriver(BundledSQLiteDriver())
            .build()
        artistDao = db.artistDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInstrumentedDaoRequirements() = runTest {
        val artist1 = ArtistEntity(id = 1, name = "Drake", genre = "Hip-Hop", yearFormed = 2006)
        val artist2 = ArtistEntity(id = 2, name = "Future", genre = "Hip-Hop", yearFormed = 2003)
        val artist3 = ArtistEntity(id = 3, name = "Lil Wayne", genre = "Hip-Hop", yearFormed = 1996)

        artistDao.upsertArtist(artist1)
        artistDao.upsertArtist(artist2)
        artistDao.upsertArtist(artist3)

        val orderedArtists = artistDao.observeAllOrderedByName().first()
        assertEquals(3, orderedArtists.size)
        assertEquals("Drake", orderedArtists[0].name)
        assertEquals("Future", orderedArtists[1].name)
        assertEquals("Lil Wayne", orderedArtists[2].name)

        val prefixResults = artistDao.searchByPrefix("dra").first()
        assertEquals(1, prefixResults.size)
        assertEquals("Drake", prefixResults[0].name)

        val updatedArtist1 = artist1.copy(genre = "Rap")
        artistDao.upsertArtist(updatedArtist1)

        val allArtistsAfterUpsert = artistDao.observeAllOrderedByName().first()
        assertEquals(3, allArtistsAfterUpsert.size)
        val fetchedDrake = allArtistsAfterUpsert.find { it.id == 1 }
        assertEquals("Rap", fetchedDrake?.genre)
    }
}