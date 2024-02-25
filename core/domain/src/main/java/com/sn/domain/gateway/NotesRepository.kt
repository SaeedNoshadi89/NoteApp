package com.sn.domain.gateway

import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun createNote(
        title: String,
        description: String,
        dueDateTime: String,
        categoryId: Int
    ): String

    suspend fun getAllNotes(categoryId: Int): Flow<Result<List<Note>>>

    fun getNoteById(noteId: String): Flow<Result<Note?>>

    suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        categoryId: Int
    )

    suspend fun deleteNote(noteId: String)

    suspend fun completeNote(noteId: String)

    suspend fun activateNote(noteId: String)

    suspend fun clearCompletedNotes()

    suspend fun deleteAllNotes()
}