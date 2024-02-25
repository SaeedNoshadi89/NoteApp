package com.sn.data.repository

import com.sn.domain.gateway.EditNoteRepository
import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditNoteRepositoryImpl @Inject constructor(): EditNoteRepository {
    override fun getNoteById(id: Int): Flow<Result<Note>> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertNote(noteId: Int) {
        TODO("Not yet implemented")
    }
}