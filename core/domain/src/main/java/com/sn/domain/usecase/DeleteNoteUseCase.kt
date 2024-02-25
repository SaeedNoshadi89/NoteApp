package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

class DeleteNoteUseCase constructor(private val repository: NotesRepository) {
    operator fun invoke(note: Note): Flow<Result<Unit>> =
        repository.deleteNote(note)
}