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
fun backupsToDelete(fileNames: List<String>, keep: Int): List<String> {
    val backups = fileNames
        .mapNotNull { name -> BackupFiles.timeOf(name)?.let { time -> name to time } }
        .sortedWith(compareByDescending<Pair<String, Long>> { it.second }.thenByDescending { it.first })

    val keepCount = keep.coerceAtLeast(0)
    if (backups.size <= keepCount) return emptyList()
    return backups.drop(keepCount).map { it.first }
}