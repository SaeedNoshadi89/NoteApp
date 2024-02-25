package com.sn.domain.gateway

import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

interface EditNoteRepository {
    fun getNoteById(id: Int): Flow<Result<Note>>
    suspend fun upsertNote(noteId: Int)
}