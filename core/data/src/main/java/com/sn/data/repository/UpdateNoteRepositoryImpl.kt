package com.sn.data.repository

import com.sn.data.data_sourse.LocalDataSource
import com.sn.data.ext.toEntity
import com.sn.data.ext.toModel
import com.sn.domain.gateway.UpdateNoteRepository
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateNoteRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
) : UpdateNoteRepository {
    override suspend fun getNoteById(noteId: String): Flow<Note?> = flow {
        emit(localDataSource.getNoteById(noteId)?.toModel())
    }

    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
    ) {
        val note = Note(
            title = title,
            description = description,
            id = noteId,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
        )

        localDataSource.upsertNote(note.toEntity())
    }

}