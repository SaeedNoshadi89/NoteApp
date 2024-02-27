package com.sn.domain.gateway

import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface UpdateNoteRepository {
    suspend fun getNoteById(noteId: String): Flow<Note?>

    suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
    )
}