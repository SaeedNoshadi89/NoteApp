package com.sn.domain.usecase

import com.sn.domain.gateway.AddAndEditNoteRepository
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(private val repository: AddAndEditNoteRepository) {
    suspend operator fun invoke(noteId: String): Flow<Note?> =
        repository.getNoteById(noteId)

}