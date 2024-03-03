package com.sn.shared_test.data.repository

import androidx.annotation.VisibleForTesting
import com.sn.data.data_sourse.LocalData
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.CalendarUiModel
import com.sn.domain.model.Category
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

class FakeNotesRepository : NotesRepository {
    private var shouldThrowError = false
    private val _savedNotes: MutableStateFlow<LinkedHashMap<String, Note>?> =
        MutableStateFlow(LinkedHashMap())
    val savedNotes: StateFlow<LinkedHashMap<String, Note>?> = _savedNotes.asStateFlow()

    private val _savedCalendar: MutableStateFlow<CalendarUiModel?> = MutableStateFlow(null)
    val savedCalendar: StateFlow<CalendarUiModel?> = _savedCalendar.asStateFlow()

    private val observableNotes: Flow<List<Note>?> = savedNotes.map {
        if (shouldThrowError) {
            throw Exception("Test exception")
        } else {
            it?.values?.toList()
        }
    }

    fun setShouldThrowError(value: Boolean) {
        shouldThrowError = value
    }

    override fun getAllNotes(categoryId: Int?, selectedDate: LocalDate?): Flow<List<Note>> = flow {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        observableNotes.first()?.let { emit(it) }
    }

    override fun getNoteById(noteId: String): Flow<Note?> = flow {
        if (shouldThrowError) {
            throw Exception("Test exception")
        }
        emit(savedNotes.value?.get(noteId))
    }

    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        dueDateTime: String,
        isCompleted: Boolean
    ) {
        val updatedNote = _savedNotes.value?.get(noteId)?.copy(
            title = title,
            description = description
        ) ?: throw Exception("Note (id $noteId) not found")

        saveNote(updatedNote)
    }

    override suspend fun deleteNote(noteId: String) {
        _savedNotes.update { notes ->
            val newNotes = LinkedHashMap<String, Note>(notes)
            newNotes.remove(noteId)
            newNotes
        }
    }

    override suspend fun completeNote(noteId: String) {
        _savedNotes.value?.get(noteId)?.let {
            saveNote(it.copy(isCompleted = true))
        }
    }

    private fun saveNote(note: Note) {
        _savedNotes.update { notes ->
            val newNotes = LinkedHashMap<String, Note>(notes)
            note.id?.let {
                newNotes[it] = note
                newNotes
            }
        }
    }

    override suspend fun activateNote(noteId: String) {
        _savedNotes.value?.get(noteId)?.let {
            saveNote(it.copy(isCompleted = false))
        }
    }

    override suspend fun clearCompletedNotes() {
        _savedNotes.update { notes ->
            notes?.filterValues {
                !it.isCompleted
            } as LinkedHashMap<String, Note>
        }
    }

    override fun getCalendar(
        startDate: LocalDate,
        lastSelectedDate: LocalDate
    ): Flow<CalendarUiModel> = flow {
        _savedCalendar.update { generateCalendar() }
        savedCalendar.value?.let { emit(it) }
    }

    override fun setDateToCalendar(date: LocalDate): Flow<CalendarUiModel> = flow {

        val mockVisibleDates = listOf(
            CalendarUiModel.Date(date, isSelected = true, isToday = false),
        )
        val newCalendarUiModel = CalendarUiModel(
            selectedDate = mockVisibleDates[0],
            visibleDates = mockVisibleDates
        )

        _savedCalendar.emit(newCalendarUiModel)
    }

    override fun getCategories(): Flow<List<Category>> = flow{
        emit(LocalData.category)
    }

    @VisibleForTesting
    fun addNotes(vararg notes: Note) {
        _savedNotes.update { oldNotes ->
            val newNotes = LinkedHashMap<String, Note>(oldNotes)
            for (note in notes) {
                note.id?.let {
                    newNotes[it] = note
                }
            }
            newNotes
        }
    }

    @VisibleForTesting
    fun generateCalendar(): CalendarUiModel {
        val mockToday = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val mockDate1 = CalendarUiModel.Date(
            date = mockToday,
            isSelected = true,
            isToday = true,
        )

        val mockDate2 = CalendarUiModel.Date(
            date = mockToday.plus(1, DateTimeUnit.DAY),
            isSelected = false,
            isToday = false,
        )

        val mockDates = listOf(mockDate1, mockDate2)

        return CalendarUiModel(
            selectedDate = mockDate1,
            visibleDates = mockDates
        )
    }
}