package com.sn.data.repository

import com.sn.data.di.DefaultDispatcher
import com.sn.domain.gateway.NotesRepository
import com.sn.data.data_sourse.LocalDataSource
import com.sn.data.ext.toEntity
import com.sn.data.ext.toModel
import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : NotesRepository {
    override suspend fun createNote(
        title: String,
        description: String,
        dueDateTime: String,
        categoryId: Int
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
            category = categoryId,
        )
        localDataSource.upsertNote(note.toEntity())
        return noteId
    }

    override suspend fun getAllNotes(categoryId: Int): Flow<Result<List<Note>>> = flow {
        emit(Result.Success(localDataSource.getAllNotes(categoryId)))
    }

    override fun getNoteById(noteId: String): Flow<Result<Note?>> = flow {
        emit(Result.Success(localDataSource.getNoteById(noteId)?.toModel()))
    }


    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        categoryId: Int
    ) {
        val note = localDataSource.getNoteById(noteId)?.copy(
            title = title,
            description = description,
            id = noteId,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = categoryId
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