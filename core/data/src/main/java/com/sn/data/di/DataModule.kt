package com.sn.data.di

import android.content.Context
import androidx.room.Room
import com.sn.data.data_sourse.LocalDataSourceImpl
import com.sn.data.local.database.NoteDao
import com.sn.data.local.database.NoteDatabase
import com.sn.data.repository.EditNoteRepositoryImpl
import com.sn.data.repository.NotesRepositoryImpl
import com.sn.domain.gateway.EditNoteRepository
import com.sn.domain.gateway.NotesRepository
import com.sn.data.data_sourse.LocalDataSource
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
    abstract fun bindEditNoteRepository(repository: EditNoteRepositoryImpl): EditNoteRepository

    @Singleton
    @Binds
    abstract fun bindNotesRepository(repository: NotesRepositoryImpl): NotesRepository
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
