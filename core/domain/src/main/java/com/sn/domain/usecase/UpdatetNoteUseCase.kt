package com.sn.domain.usecase

import com.sn.domain.gateway.AddAndEditNoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(private val repository: AddAndEditNoteRepository) {
    suspend operator fun invoke(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        category: Int
    ) =
        repository.updateNote(
            noteId = noteId,
            title = title,
            description = description,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = category
        )
}