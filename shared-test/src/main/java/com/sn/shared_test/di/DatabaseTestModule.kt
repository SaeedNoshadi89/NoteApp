package com.sn.shared_test.di

import android.content.Context
import androidx.room.Room
import com.sn.data.di.DatabaseModule
import com.sn.data.local.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object DatabaseTestModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): NoteDatabase {
        return Room
            .inMemoryDatabaseBuilder(context.applicationContext, NoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}
