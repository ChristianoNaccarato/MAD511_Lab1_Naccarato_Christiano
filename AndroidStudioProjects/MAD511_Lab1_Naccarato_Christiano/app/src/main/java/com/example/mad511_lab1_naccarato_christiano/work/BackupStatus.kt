package com.example.mad511_lab1_naccarato_christiano.work

import kotlinx.coroutines.flow.Flow

sealed interface BackupStatus {
    data object NothingRun : BackupStatus
    data object Running : BackupStatus
    data class Finished(val artistCount: Int) : BackupStatus
    data object Failed : BackupStatus
}

interface BackupActions {
    fun backupNow()
    fun status(): Flow<BackupStatus>
}