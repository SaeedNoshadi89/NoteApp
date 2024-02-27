package com.sn.domain.usecase

import com.sn.domain.gateway.UpdateNoteRepository
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow

class GetNoteByIdUseCase constructor(private val repository: UpdateNoteRepository) {
    suspend operator fun invoke(noteId: String): Flow<Note?> =
        repository.getNoteById(noteId)

}