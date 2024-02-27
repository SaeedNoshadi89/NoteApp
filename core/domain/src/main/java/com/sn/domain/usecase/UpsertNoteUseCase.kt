package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository

class UpsertNoteUseCase constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
    ) =
        repository.updateNote(
            noteId = noteId,
            title = title,
            description = description,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
        )
}