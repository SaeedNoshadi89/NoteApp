package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(noteId: String) =
        repository.deleteNote(noteId)
}