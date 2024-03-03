package com.sn.data.repository

import com.sn.core.getDelayInMilliseconds
import com.sn.data.data_sourse.LocalData
import com.sn.data.data_sourse.LocalDataSource
import com.sn.data.data_sourse.ReminderScheduler
import com.sn.data.di.DefaultDispatcher
import com.sn.data.ext.toEntity
import com.sn.data.ext.toModel
import com.sn.domain.gateway.AddAndEditNoteRepository
import com.sn.domain.model.Category
import com.sn.domain.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

class AddAndEditNoteRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    private val reminderScheduler: ReminderScheduler?
) : AddAndEditNoteRepository {
    override suspend fun createNote(
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        category: Int,
    ): String {

        val noteId = withContext(dispatcher) {
            UUID.randomUUID().toString()
        }
        val note = Note(
            title = title,
            description = description,
            id = noteId,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = category
        )


        if (dueDateTime.isNotEmpty()) {
            reminderScheduler?.scheduleReminder(
                noteId = noteId,
                dueDateTime = getDelayInMilliseconds(
                    dueDateTime.toLong(),
                    Clock.System.now()
                ),
                noteTitle = title,
                noteDescription = description
            )
        }

        localDataSource.upsertNote(note.toEntity())
        return noteId
    }

    override suspend fun getNoteById(noteId: String): Flow<Note?> = flow {
        emit(localDataSource.getNoteById(noteId)?.toModel())
    }

    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        category: Int,
    ) {
        val note = Note(
            title = title,
            description = description,
            id = noteId,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = category
        )
        if (dueDateTime.isNotEmpty()) {
            reminderScheduler?.scheduleReminder(
                noteId = noteId,
                dueDateTime = getDelayInMilliseconds(
                    dueDateTime.toLong(),
                    Clock.System.now()
                ),
                noteTitle = title,
                noteDescription = description
            )
        }

        localDataSource.upsertNote(note.toEntity())
    }

    override fun getCategories(): Flow<List<Category>> = flowOf(LocalData.category)

}