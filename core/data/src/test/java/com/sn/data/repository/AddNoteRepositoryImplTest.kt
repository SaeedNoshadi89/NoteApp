package com.sn.data.repository

import app.cash.turbine.turbineScope
import com.google.common.truth.Truth.assertThat
import com.sn.data.data_sourse.CalendarDataSource
import com.sn.data.ext.toEntity
import com.sn.domain.gateway.AddAndEditNoteRepository
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import com.sn.shared_test.FakeCalendarDataSource
import com.sn.shared_test.FakeLocalDataSource
import com.sn.shared_test.FakeNoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddNoteRepositoryImplTest {
    private val note = Note(
        id = "1",
        title = "Title",
        description = "Description",
        dueDateTime = "123456",
        isCompleted = false,
        category = 1
    )
    private val updatedTitle = "updated title"
    private val updatedDesc = "updated desc"
    private val updatedDueDateTime = "1234567"

    private val localNotes = listOf(note.toEntity())
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var noteDao: FakeNoteDao
    private var testDispatcher = UnconfinedTestDispatcher()
    private lateinit var notesRepository: NotesRepository
    private lateinit var addNoteRepository: AddAndEditNoteRepository
    private lateinit var calendarDataSource: CalendarDataSource

    @ExperimentalCoroutinesApi
    @Before
    fun initRepository() {
        localDataSource = FakeLocalDataSource(localNotes.toMutableList())
        calendarDataSource = FakeCalendarDataSource()
        noteDao = FakeNoteDao(localNotes)
        // Get a reference to the class under test
        notesRepository =
            NotesRepositoryImpl(localDataSource = localDataSource, calendarDataSource)

        addNoteRepository =
            AddAndEditNoteRepositoryImpl(
                localDataSource = localDataSource,
                reminderScheduler = null,
                dispatcher = testDispatcher
            )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun addANote_getNotesCache() = runTest {
        turbineScope {
            // get all notes before create a new
            val oldNotes = notesRepository.getAllNotes(1, null).testIn(backgroundScope).awaitItem()
            assertThat(oldNotes.size).isEqualTo(1)
            //Save a note
            addNoteRepository.createNote(
                title = "new title",
                description = "new description",
                dueDateTime = "1234567",
                isCompleted = false,
                category = 1,
            )

            val newNotes = notesRepository.getAllNotes(1, null).testIn(backgroundScope).awaitItem()
            assertThat(newNotes.size).isEqualTo(2)
        }
    }


    @ExperimentalCoroutinesApi
    @Test
    fun getNotesById_UpdateNoteInCache() = runTest {
        turbineScope {
            // Save a note
            val newNoteId =
                addNoteRepository.createNote(
                    title = note.title,
                    description = note.description ?: "",
                    dueDateTime = note.dueDateTime,
                    isCompleted = note.isCompleted,
                    category = 1,
                )

            // Update the note
            addNoteRepository.updateNote(
                noteId = newNoteId,
                title = updatedTitle,
                description = updatedDesc,
                dueDateTime = updatedDueDateTime,
                isCompleted = note.isCompleted,
                category = 1
            )
            // Get a note by id after update
            val noteAfterUpdate =
                addNoteRepository.getNoteById(newNoteId).testIn(backgroundScope).awaitItem()
            assertThat(noteAfterUpdate?.title).isEqualTo(updatedTitle)
            assertThat(noteAfterUpdate?.description).isEqualTo(updatedDesc)
            assertThat(noteAfterUpdate?.dueDateTime).isEqualTo(updatedDueDateTime)
            assertThat(noteAfterUpdate?.isCompleted).isEqualTo(note.isCompleted)
        }
    }
}