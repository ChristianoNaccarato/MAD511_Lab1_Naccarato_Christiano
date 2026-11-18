package com.example.mad511_lab1_naccarato_christiano.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ArtistRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            val artists = repository.observeAll().first()

            if (artists.isEmpty()) {
                return Result.success(workDataOf(KEY_COUNT to 0))
            }

            val now = System.currentTimeMillis()
            val directory = File(applicationContext.filesDir, BackupFiles.DIRECTORY)
            if (!directory.exists() && !directory.mkdirs() && !directory.exists()) {
                throw IOException("Could not create ${directory.path}")
            }

            val fileName = BackupFiles.fileName(now)
            val backup = BackupFile(
                createdAt = now,
                artists = artists.map { it.toBackupArtist() }
            )
            File(directory, fileName).writeText(Json.encodeToString(backup))

            return Result.success(
                workDataOf(
                    KEY_COUNT to artists.size,
                    KEY_FILE_NAME to fileName
                )
            )
        } catch (e: IOException) {
            return if (runAttemptCount < MAX_ATTEMPTS) Result.retry() else Result.failure()
        }
    }

    companion object {
        const val KEY_COUNT = "artist_count"
        const val KEY_FILE_NAME = "file_name"
        const val MAX_ATTEMPTS = 3
    }
}