package com.sn.domain.gateway

import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotes(categoryId: Int): Flow<Result<List<Note>>>
    suspend fun upsertNote(noteId: Int)
    fun deleteNote(note: Note): Flow<Result<Unit>>
    suspend fun completeTask(taskId: String)
}