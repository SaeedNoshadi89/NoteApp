package com.sn.domain.gateway

import com.sn.domain.model.CalendarUiModel
import com.sn.domain.model.Category
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface NotesRepository {

    fun getAllNotes(categoryId: Int?, selectedDate: LocalDate?): Flow<List<Note>>

    fun getNoteById(noteId: String): Flow<Note?>

    suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean,
    )

    suspend fun deleteNote(noteId: String)

    suspend fun completeNote(noteId: String)

    suspend fun activateNote(noteId: String)

    suspend fun clearCompletedNotes()

    suspend fun deleteAllNotes()

    fun getCalendar(startDate: LocalDate, lastSelectedDate: LocalDate): Flow<CalendarUiModel>
    fun setDateToCalendar(date: LocalDate): Flow<CalendarUiModel>

    fun getCategories(): Flow<List<Category>>
}