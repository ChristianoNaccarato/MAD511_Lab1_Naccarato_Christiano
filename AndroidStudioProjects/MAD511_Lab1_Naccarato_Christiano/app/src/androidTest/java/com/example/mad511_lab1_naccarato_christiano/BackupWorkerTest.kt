package com.example.mad511_lab1_naccarato_christiano

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.mad511_lab1_naccarato_christiano.data.Artist
import com.example.mad511_lab1_naccarato_christiano.data.ArtistRepository
import com.example.mad511_lab1_naccarato_christiano.work.BackupFiles
import com.example.mad511_lab1_naccarato_christiano.work.BackupWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException

private class FakeBackupRepository(
    artists: List<Artist> = emptyList(),
    private val failWith: IOException? = null
) : ArtistRepository {
    private val state = MutableStateFlow(artists)

    override fun observeAll(): Flow<List<Artist>> = flow {
        if (failWith != null) throw failWith
        emit(state.value)
    }

    override fun observeById(id: Int): Flow<Artist?> = state.map { list -> list.find { it.id == id } }
    override fun search(query: String): Flow<List<Artist>> = state
    override suspend fun addArtist(artist: Artist) { state.value = state.value + artist }
    override suspend fun updateArtist(artist: Artist) {}
    override suspend fun deleteArtist(artist: Artist) {}
}

@RunWith(AndroidJUnit4::class)
class BackupWorkerTest {

    private lateinit var context: Context
    private lateinit var backupDir: File

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        backupDir = File(context.filesDir, BackupFiles.DIRECTORY)
        backupDir.deleteRecursively()
    }

    @After
    fun tearDown() {
        backupDir.deleteRecursively()
    }

    private fun buildWorker(repository: ArtistRepository, attempt: Int = 0): BackupWorker {
        val factory = object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker? {
                return if (workerClassName == BackupWorker::class.java.name) {
                    BackupWorker(appContext, workerParameters, repository)
                } else {
                    null
                }
            }
        }
        return TestListenableWorkerBuilder<BackupWorker>(context)
            .setWorkerFactory(factory)
            .setRunAttemptCount(attempt)
            .build()
    }

    @Test
    fun repositoryWithArtists_returnsSuccess_andOutputCountMatches() = runTest {
        val repo = FakeBackupRepository(
            listOf(
                Artist(id = 1, name = "Drake", genre = "Hip-Hop", yearFormed = 2006),
                Artist(id = 2, name = "Future", genre = "Hip-Hop", yearFormed = 2003)
            )
        )

        val result = buildWorker(repo).doWork()

        assertTrue(result is ListenableWorker.Result.Success)
        val output = (result as ListenableWorker.Result.Success).outputData
        assertEquals(2, output.getInt(BackupWorker.KEY_COUNT, -1))
        val fileName = output.getString(BackupWorker.KEY_FILE_NAME)
        assertTrue(File(backupDir, fileName!!).exists())
    }

    @Test
    fun emptyRepository_returnsSuccess_andWritesNoFile() = runTest {
        val result = buildWorker(FakeBackupRepository()).doWork()

        assertTrue(result is ListenableWorker.Result.Success)
        assertTrue(backupDir.list().isNullOrEmpty())
    }

    @Test
    fun repositoryThrowingIOException_returnsRetryOnFirstAttempt() = runTest {
        val repo = FakeBackupRepository(failWith = IOException("disk went away"))

        val result = buildWorker(repo, attempt = 0).doWork()

        assertTrue(result is ListenableWorker.Result.Retry)
    }

    @Test
    fun repositoryThrowingIOException_returnsFailureOnceAttemptCountReachesThree() = runTest {
        val repo = FakeBackupRepository(failWith = IOException("disk went away"))

        val result = buildWorker(repo, attempt = 3).doWork()

        assertTrue(result is ListenableWorker.Result.Failure)
        assertFalse(result is ListenableWorker.Result.Retry)
    }
}