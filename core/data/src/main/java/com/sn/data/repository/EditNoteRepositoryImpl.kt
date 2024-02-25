package com.sn.data.repository

import com.sn.data.di.ApplicationScope
import com.sn.data.di.DefaultDispatcher
import com.sn.domain.gateway.EditNoteRepository
import com.sn.data.data_sourse.LocalDataSource
import com.sn.data.ext.toEntity
import com.sn.data.ext.toModel
import com.sn.domain.model.Note
import com.sn.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EditNoteRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
) : EditNoteRepository {
    override suspend fun getNoteById(noteId: String): Flow<Result<Note?>> = flow {
        emit(Result.Success(localDataSource.getNoteById(noteId)?.toModel()))
    }



    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
        categoryId: Int
    ) {
        val note = localDataSource.getNoteById(noteId)?.copy(
            title = title,
            description = description,
            id = noteId,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = categoryId
        ) ?: throw Exception("Note (id $noteId) not found")

        localDataSource.upsertNote(note)
    }

}