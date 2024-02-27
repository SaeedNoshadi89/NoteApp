package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository

class DeleteNoteUseCase constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(noteId: String) =
        repository.deleteNote(noteId)
}