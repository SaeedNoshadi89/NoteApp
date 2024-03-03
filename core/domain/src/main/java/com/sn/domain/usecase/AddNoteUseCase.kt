package com.sn.domain.usecase

import com.sn.domain.gateway.AddAndEditNoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(private val repository: AddAndEditNoteRepository) {
    suspend operator fun invoke(
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        category: Int
    ) =
        repository.createNote(
            title = title,
            description = description,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = category
        )
}