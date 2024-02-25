package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

class DeleteNoteUseCase constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(noteId: String) =
        repository.deleteNote(noteId)
}