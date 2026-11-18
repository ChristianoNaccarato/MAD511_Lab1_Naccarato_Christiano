package com.example.mad511_lab1_naccarato_christiano.work

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

const val PERIODIC_BACKUP_NAME = "setlist_periodic_backup"
const val PERIODIC_CLEANUP_NAME = "setlist_periodic_cleanup"
const val BACKUP_NOW_NAME = "setlist_backup_now"

private const val BACKUP_TAG = "setlist_backup_worker"
private const val BACKUP_INTERVAL_HOURS = 24L
private const val CLEANUP_INTERVAL_DAYS = 7L
private const val BACKOFF_SECONDS = 30L
private const val KEEP_COUNT = 5

@Singleton
class BackupScheduler @Inject constructor(
    private val workManager: WorkManager
) : BackupActions {

    private val backupConstraints = Constraints.Builder()
        .setRequiresStorageNotLow(true)
        .setRequiresBatteryNotLow(true)
        .build()

    private val cleanupConstraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()

    private val keepData = workDataOf(CleanupWorker.KEY_KEEP to KEEP_COUNT)

    fun scheduleBackups() {
        val backup = PeriodicWorkRequestBuilder<BackupWorker>(BACKUP_INTERVAL_HOURS, TimeUnit.HOURS)
            .setConstraints(backupConstraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_SECONDS, TimeUnit.SECONDS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            PERIODIC_BACKUP_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            backup
        )

        val cleanup = PeriodicWorkRequestBuilder<CleanupWorker>(CLEANUP_INTERVAL_DAYS, TimeUnit.DAYS)
            .setConstraints(cleanupConstraints)
            .setInputData(keepData)
            .build()
        workManager.enqueueUniquePeriodicWork(
            PERIODIC_CLEANUP_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            cleanup
        )
    }

    override fun backupNow() {
        val backup = OneTimeWorkRequestBuilder<BackupWorker>()
            .setConstraints(backupConstraints)
            .addTag(BACKUP_TAG)
            .build()
        val cleanup = OneTimeWorkRequestBuilder<CleanupWorker>()
            .setConstraints(cleanupConstraints)
            .setInputData(keepData)
            .build()

        workManager.beginUniqueWork(BACKUP_NOW_NAME, ExistingWorkPolicy.KEEP, backup)
            .then(cleanup)
            .enqueue()
    }

    override fun status(): Flow<BackupStatus> {
        return workManager.getWorkInfosForUniqueWorkFlow(BACKUP_NOW_NAME).map { infos ->
            when {
                infos.isEmpty() -> BackupStatus.NothingRun
                infos.any {
                    it.state == WorkInfo.State.ENQUEUED ||
                            it.state == WorkInfo.State.BLOCKED ||
                            it.state == WorkInfo.State.RUNNING
                } -> BackupStatus.Running
                infos.any { it.state == WorkInfo.State.FAILED } -> BackupStatus.Failed
                infos.all { it.state == WorkInfo.State.SUCCEEDED } -> {
                    val backupInfo = infos.firstOrNull { BACKUP_TAG in it.tags }
                    BackupStatus.Finished(backupInfo?.outputData?.getInt(BackupWorker.KEY_COUNT, 0) ?: 0)
                }
                else -> BackupStatus.NothingRun
            }
        }
    }

    fun cancelAll() {
        workManager.cancelUniqueWork(PERIODIC_BACKUP_NAME)
        workManager.cancelUniqueWork(PERIODIC_CLEANUP_NAME)
        workManager.cancelUniqueWork(BACKUP_NOW_NAME)
    }
}