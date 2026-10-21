package com.example.mad511_lab1_naccarato_christiano.data

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers


@Database(entities = [ArtistEntity::class], version = 1, exportSchema = true)
@ColumnTypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ArtistDao(): ArtistDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context.applicationContext).also { INSTANCE = it }
            }

        private fun build(context: Context) =
            Room.databaseBuilder<AppDatabase>(context, "Artists.db")
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
    }
}