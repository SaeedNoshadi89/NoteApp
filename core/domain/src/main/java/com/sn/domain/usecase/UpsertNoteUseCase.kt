package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

class UpsertNoteUseCase constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        categoryId: Int
    ) =
        repository.updateNote(
            noteId = noteId,
            title = title,
            description = description,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            categoryId = categoryId
        )
}