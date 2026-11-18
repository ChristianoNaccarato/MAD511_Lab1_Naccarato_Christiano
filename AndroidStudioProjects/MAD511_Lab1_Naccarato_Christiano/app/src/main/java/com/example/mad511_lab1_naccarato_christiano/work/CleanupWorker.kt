package com.example.mad511_lab1_naccarato_christiano.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.example.mad511_lab1_naccarato_christiano.work.BackupFiles
import java.io.File

@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val keep = inputData.getInt(KEY_KEEP, DEFAULT_KEEP)

        val directory = File(applicationContext.filesDir, BackupFiles.DIRECTORY)
        val names = directory.list()?.toList() ?: emptyList()

        var deleted = 0
        for (name in backupsToDelete(names, keep)) {
            if (File(directory, name).delete()) deleted++
        }

        return Result.success(workDataOf(KEY_DELETED to deleted))
    }

    companion object {
        const val KEY_KEEP = "keep"
        const val KEY_DELETED = "deleted_count"
        const val DEFAULT_KEEP = 5
    }
}