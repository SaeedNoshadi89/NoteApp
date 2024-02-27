package com.sn.data.repository

import com.sn.data.data_sourse.LocalDataSource
import com.sn.data.di.DefaultDispatcher
import com.sn.data.ext.toEntity
import com.sn.domain.gateway.AddNoteRepository
import com.sn.domain.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class AddNoteRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : AddNoteRepository {
    override suspend fun createNote(
        title: String,
        description: String,
        dueDateTime: String,
    ): String {
        val noteId = withContext(dispatcher) {
            UUID.randomUUID().toString()
        }
        val note = Note(
            title = title,
            description = description,
            id = noteId,
            dueDateTime = dueDateTime,
            isCompleted = false,
        )
        localDataSource.upsertNote(note.toEntity())
        return noteId
    }

}