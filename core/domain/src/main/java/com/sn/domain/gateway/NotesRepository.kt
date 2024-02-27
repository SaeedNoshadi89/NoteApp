package com.sn.domain.gateway

import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun getAllNotes(): Flow<List<Note>>

    fun getNoteById(noteId: String): Flow<Note?>

    suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
    )

    suspend fun deleteNote(noteId: String)

    suspend fun completeNote(noteId: String)

    suspend fun activateNote(noteId: String)

    suspend fun clearCompletedNotes()

    suspend fun deleteAllNotes()
}