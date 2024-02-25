package com.sn.domain.gateway

import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

interface EditNoteRepository {
    suspend fun getNoteById(noteId: String): Flow<Result<Note?>>

    suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        categoryId: Int
    )
}