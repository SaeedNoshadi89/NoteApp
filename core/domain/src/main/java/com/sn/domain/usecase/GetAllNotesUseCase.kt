package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase constructor(private val repository: NotesRepository) {
    suspend operator fun invoke(): Flow<List<Note>> =
        repository.getAllNotes()
}