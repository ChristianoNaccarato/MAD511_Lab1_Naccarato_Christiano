package com.example.mad511_lab1_naccarato_christiano.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.mad511_lab1_naccarato_christiano.work.BackupActions
import com.example.mad511_lab1_naccarato_christiano.work.BackupScheduler

@Module
@InstallIn(SingletonComponent::class)
object WorkModule {

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
        @Provides
        @Singleton
        fun provideBackupActions(scheduler: BackupScheduler): BackupActions = scheduler
}