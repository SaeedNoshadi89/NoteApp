package com.sn.domain.gateway.data_sourse

import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllNotes(categoryId: Int): Flow<Result<List<Note>>>
    suspend fun upsertNote(noteId: Int)
    fun deleteNote(note: Note): Flow<Result<Unit>>
    suspend fun completeNote(taskId: String)
    suspend fun activateNote(taskId: String)
    fun getNoteById(id: Int): Flow<Result<Note>>
    suspend fun clearCompletedNotes()

    suspend fun deleteAllNotes()
}