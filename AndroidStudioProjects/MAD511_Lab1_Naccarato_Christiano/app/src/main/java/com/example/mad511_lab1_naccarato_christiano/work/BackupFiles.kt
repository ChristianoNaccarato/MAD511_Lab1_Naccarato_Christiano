package com.example.mad511_lab1_naccarato_christiano.work

object BackupFiles {
    const val DIRECTORY = "backups"
    const val PREFIX = "setlist_backup_"
    const val SUFFIX = ".json"

    private val pattern = Regex("^${PREFIX}(\\d+)${Regex.escape(SUFFIX)}$")

    fun fileName(timeMillis: Long): String = "$PREFIX$timeMillis$SUFFIX"

    fun timeOf(fileName: String): Long? {
        val digits = pattern.matchEntire(fileName)?.groupValues?.get(1) ?: return null
        return digits.toLongOrNull()
    }
}