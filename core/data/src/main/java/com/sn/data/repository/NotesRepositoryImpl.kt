package com.sn.data.repository

import com.sn.data.di.DefaultDispatcher
import com.sn.domain.gateway.NotesRepository
import com.sn.data.data_sourse.LocalDataSource
import com.sn.data.ext.toEntity
import com.sn.data.ext.toModel
import com.sn.domain.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
) : NotesRepository {

    override suspend fun getAllNotes(): Flow<List<Note>> = flow {
        emit(localDataSource.getAllNotes())
    }

    override fun getNoteById(noteId: String): Flow<Note?> = flow {
        emit(localDataSource.getNoteById(noteId)?.toModel())
    }


    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
    ) {
        val note = localDataSource.getNoteById(noteId)?.copy(
            title = title,
            description = description,
            id = noteId,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
        ) ?: throw Exception("Note (id $noteId) not found")

        localDataSource.upsertNote(note)
    }


    override suspend fun deleteNote(noteId: String) {
        localDataSource.deleteNote(noteId)
    }

    override suspend fun completeNote(noteId: String) {
        localDataSource.completeNote(noteId)
    }

    override suspend fun activateNote(noteId: String) {
        localDataSource.activateNote(noteId)
    }

    override suspend fun clearCompletedNotes() {
        localDataSource.clearCompletedNotes()
    }

    override suspend fun deleteAllNotes() {
        localDataSource.deleteAllNotes()
    }

}