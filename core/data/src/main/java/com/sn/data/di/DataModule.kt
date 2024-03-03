package com.sn.data.di

import android.content.Context
import androidx.room.Room
import com.sn.data.data_sourse.CalendarDataSource
import com.sn.data.data_sourse.CalendarDataSourceImpl
import com.sn.data.data_sourse.LocalDataSource
import com.sn.data.data_sourse.LocalDataSourceImpl
import com.sn.data.data_sourse.ReminderScheduler
import com.sn.data.data_sourse.ReminderSchedulerImpl
import com.sn.data.local.database.NoteDao
import com.sn.data.local.database.NoteDatabase
import com.sn.data.repository.AddAndEditNoteRepositoryImpl
import com.sn.data.repository.NotesRepositoryImpl
import com.sn.domain.gateway.AddAndEditNoteRepository
import com.sn.domain.gateway.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindNotesRepository(repository: NotesRepositoryImpl): NotesRepository

    @Singleton
    @Binds
    abstract fun bindAddNoteRepository(repository: AddAndEditNoteRepositoryImpl): AddAndEditNoteRepository

    @Singleton
    @Binds
    abstract fun bindReminderScheduler(reminderScheduler: ReminderSchedulerImpl): ReminderScheduler

    @Singleton
    @Binds
    abstract fun bindCalendarDataSource(calendarDataSourceImpl: CalendarDataSourceImpl): CalendarDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindLocalDataSource(dataSource: LocalDataSourceImpl): LocalDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "Notes.db"
        ).build()
    }

    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()
}
