package com.sn.data.data_sourse

import com.sn.data.entity.NoteEntity
import com.sn.domain.model.Note

interface LocalDataSource {
    suspend fun getAllNotes(categoryId: Int?): List<Note>

    suspend fun upsertNote(note: NoteEntity)

    suspend fun deleteNote(noteId: String): Int

    suspend fun completeNote(noteId: String)

    suspend fun activateNote(noteId: String)

    suspend fun getNoteById(noteId: String): NoteEntity?

    suspend fun clearCompletedNotes(): Int
}