package com.sn.data.data_sourse

import com.sn.data.entity.NoteEntity
import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getAllNotes(categoryId: Int): List<Note>

    suspend fun upsertNote(note: NoteEntity)

    suspend fun deleteNote(noteId: String): Int

    suspend fun completeNote(noteId: String)

    suspend fun activateNote(noteId: String)

    suspend fun getNoteById(noteId: String): NoteEntity?

    suspend fun clearCompletedNotes(): Int

    suspend fun deleteAllNotes()
}