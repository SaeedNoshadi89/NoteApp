package com.sn.shared_test

import com.sn.data.entity.NoteEntity
import com.sn.data.local.database.NoteDao

class FakeNoteDao(initialNotes: List<NoteEntity>? = emptyList()) : NoteDao {

    private var _notes: MutableMap<String, NoteEntity>? = null

    var notes: List<NoteEntity>?
        get() = _notes?.values?.toList()
        set(newNotes) {
            _notes = newNotes?.associateBy { it.id }?.toMutableMap()
        }

    init {
        notes = initialNotes
    }

    override fun getAllNotes(categoryId: Int?, selectedDate: kotlinx.datetime.LocalDate?): List<NoteEntity> = notes ?: throw Exception("Note list is null")

    override fun getNoteById(noteId: String): NoteEntity? = _notes?.get(noteId)

    override suspend fun upsertNote(note: NoteEntity) {
        _notes?.put(note.id, note)
    }

    override suspend fun updateCompleted(noteId: String, completed: Boolean) {
        _notes?.get(noteId)?.run { copy(isCompleted = completed) }
    }


    override suspend fun deleteById(noteId: String): Int {
        return if (_notes?.remove(noteId) == null) {
            0
        } else {
            1
        }
    }

    override suspend fun deleteCompleted(): Int {
        _notes?.apply {
            val originalSize = size
            entries.removeIf { it.value.isCompleted }
            return originalSize - size
        }
        return 0
    }

    override suspend fun deleteAll() {
        _notes?.clear()
    }

}
