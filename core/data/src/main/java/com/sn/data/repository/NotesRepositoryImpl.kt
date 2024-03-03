package com.sn.data.repository

import com.sn.data.data_sourse.CalendarDataSource
import com.sn.data.data_sourse.LocalData
import com.sn.data.data_sourse.LocalDataSource
import com.sn.data.ext.toModel
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.CalendarUiModel
import com.sn.domain.model.Category
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val calendarDataSource: CalendarDataSource
) : NotesRepository {

    override fun getAllNotes(categoryId: Int?, selectedDate: LocalDate?): Flow<List<Note>> = flow{
        emit(localDataSource.getAllNotes(categoryId))
    }

    override fun getNoteById(noteId: String): Flow<Note?> = flow {
        emit(localDataSource.getNoteById(noteId)?.toModel())
    }


    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
    ) {
        val note = localDataSource.getNoteById(noteId)?.copy(
            title = title,
            description = description,
            id = noteId,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
        ) ?: throw Exception("Note (id $noteId) not found")

        localDataSource.upsertNote(note)
    }


    override suspend fun deleteNote(noteId: String) {
        localDataSource.deleteNote(noteId)
    }

    override suspend fun completeNote(noteId: String) {
        localDataSource.completeNote(noteId)
    }

    override suspend fun activateNote(noteId: String) {
        localDataSource.activateNote(noteId)
    }

    override suspend fun clearCompletedNotes() {
        localDataSource.clearCompletedNotes()
    }

    override fun getCalendar(
        startDate: LocalDate,
        lastSelectedDate: LocalDate
    ): Flow<CalendarUiModel> = flowOf(
        calendarDataSource.getData(
            startDate = startDate,
            lastSelectedDate = lastSelectedDate
        )
    )

    override fun setDateToCalendar(date: LocalDate): Flow<CalendarUiModel> =
        flowOf(calendarDataSource.getData(lastSelectedDate = date))

    override fun getCategories(): Flow<List<Category>> = flowOf(LocalData.category)



}