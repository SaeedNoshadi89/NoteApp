package com.sn.domain.usecase

import com.sn.domain.gateway.EditNoteRepository
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

class GetNoteByIdUseCase constructor(private val repository: EditNoteRepository) {
    suspend operator fun invoke(noteId: String): Flow<Result<Note?>> =
        repository.getNoteById(noteId)

}