package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase constructor(private val repository: NotesRepository) {
    operator fun invoke(categoryId: Int = 1): Flow<Result<List<Note>>> =
        repository.getAllNotes(categoryId)
}