package com.sn.shared_test

import com.sn.data.data_sourse.LocalDataSource
import com.sn.data.entity.NoteEntity
import com.sn.data.ext.toModel
import com.sn.domain.model.Note

class FakeLocalDataSource(var notes: MutableList<NoteEntity>? = mutableListOf()) :
    LocalDataSource {
    override suspend fun getAllNotes(): List<Note> = notes?.map { it.toModel() } ?: throw Exception("Note list is null")

    override suspend fun upsertNote(note: NoteEntity) {
        notes?.add(note)
    }

    override suspend fun deleteNote(noteId: String): Int {
        notes?.removeIf { it.id == noteId }
        return 1
    }

    override suspend fun completeNote(noteId: String) {
        notes?.firstOrNull { it.id == noteId }?.let {
            val updatedNote = it.copy(isCompleted = true)
            notes?.remove(it)
            notes?.add(updatedNote)
        }
    }

    override suspend fun activateNote(noteId: String) {
        notes?.firstOrNull { it.id == noteId }?.let {
            val updatedNote = it.copy(isCompleted = false)
            notes?.remove(it)
            notes?.add(updatedNote)
        }
    }

    override suspend fun getNoteById(noteId: String): NoteEntity? {
        return notes?.find { it.id == noteId }
    }

    override suspend fun clearCompletedNotes(): Int {
       notes?.removeIf { it.isCompleted }
        return 1
    }

    override suspend fun deleteAllNotes() {
        notes?.clear()
    }
}