package com.sn.shared_test.data.repository

import com.sn.domain.gateway.AddAndEditNoteRepository
import com.sn.domain.model.Category
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import java.util.UUID

class FakeAddAndEditNoteRepository: AddAndEditNoteRepository {
    private fun generateNoteId() = UUID.randomUUID().toString()
    private var shouldThrowError = false
    private val _savedNotes: MutableStateFlow<LinkedHashMap<String, Note>?> = MutableStateFlow(LinkedHashMap())
    val savedNotes: StateFlow<LinkedHashMap<String, Note>?> = _savedNotes.asStateFlow()
    override suspend fun createNote(
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        category: Int
    ): String {
        val noteId = generateNoteId()
        Note(title = title, description = description, id = noteId).also {
            saveNote(it)
        }
        return noteId
    }
    fun setShouldThrowError(value: Boolean) {
        shouldThrowError = value
    }

    private fun saveNote(note: Note) {
        _savedNotes.update { notes ->
            val newNotes = LinkedHashMap<String, Note>(notes)
            note.id?.let {
                newNotes[it] = note
                newNotes
            }
        }
    }
    override suspend fun getNoteById(noteId: String): Flow<Note?> = flow{
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        emit(savedNotes.value?.get(noteId))
    }

    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        category: Int
    ) {
        val updatedNote = _savedNotes.value?.get(noteId)?.copy(
            title = title,
            description = description
        ) ?: throw Exception("Note (id $noteId) not found")

        saveNote(updatedNote)
    }

    override fun getCategories(): Flow<List<Category>> {
        TODO("Not yet implemented")
    }
}