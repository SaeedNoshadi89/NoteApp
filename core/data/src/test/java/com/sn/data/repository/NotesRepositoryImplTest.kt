package com.sn.data.repository

import app.cash.turbine.turbineScope
import com.google.common.truth.Truth.assertThat
import com.sn.data.ext.toEntity
import com.sn.domain.gateway.AddNoteRepository
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import com.sn.shared_test.FakeLocalDataSource
import com.sn.shared_test.FakeNoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesRepositoryImplTest {
    private val note1 = Note(
        id = "1",
        title = "Title1",
        description = "Description1",
        dueDateTime = "123456",
        isCompleted = false
    )
    private val note2 = Note(
        id = "2",
        title = "Title2",
        description = "Description2",
        dueDateTime = "1234567",
        isCompleted = false
    )
    private val note3 = Note(
        id = "3",
        title = "Title3",
        description = "Description3",
        dueDateTime = "12345678",
        isCompleted = false
    )

    private val newNoteTitle = "Title new"
    private val newNoteDescription = "Description new"
    private val newNote = Note(
        id = "new",
        title = newNoteTitle,
        description = newNoteDescription,
        dueDateTime = "",
        isCompleted = false
    )
    private val localNotes = listOf(note3.toEntity())
    private var testDispatcher = UnconfinedTestDispatcher()
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var noteDao: FakeNoteDao

    private lateinit var notesRepository: NotesRepository
    private lateinit var addNoteRepository: AddNoteRepository

    @ExperimentalCoroutinesApi
    @Before
    fun initRepository() {
        localDataSource = FakeLocalDataSource(localNotes.toMutableList())
        noteDao = FakeNoteDao(localNotes)
        // Get a reference to the class under test
        notesRepository =
            NotesRepositoryImpl(localDataSource = localDataSource)
        addNoteRepository =
            AddNoteRepositoryImpl(localDataSource = localDataSource, dispatcher = testDispatcher)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getNotes_emptyRepositoryAndUninitializedCache() = runTest {
        turbineScope {
            localDataSource.deleteAllNotes()

            val notes = notesRepository.getAllNotes().testIn(backgroundScope).awaitItem()
            assertThat(notes.size).isEqualTo(0)
        }
    }

    @Test
    fun completeNote_completesNote_UpdatesCache() = runTest {
        turbineScope {
            // Save a note
            val newNoteId =
                addNoteRepository.createNote(
                    newNote.title,
                    newNote.description ?: "",
                    dueDateTime = newNote.dueDateTime
                )
            // Make sure it's active
            val noteIsNotComplete =
                notesRepository.getNoteById(newNoteId).testIn(backgroundScope).awaitItem()
            assertThat(noteIsNotComplete?.isCompleted).isFalse()

            // Mark is as complete
            notesRepository.completeNote(newNoteId)

            // Verify it's now completed
            val noteIsComplete =
                notesRepository.getNoteById(newNoteId).testIn(backgroundScope).awaitItem()
            assertThat(noteIsComplete?.isCompleted).isTrue()
        }
    }

    @Test
    fun completeNote_activeNote_UpdatesCache() = runTest {
        turbineScope {
            // Save a note
            val newNoteId =
                addNoteRepository.createNote(
                    newNote.title,
                    newNote.description ?: "",
                    dueDateTime = newNote.dueDateTime
                )

            notesRepository.completeNote(newNoteId)

            val note =
                notesRepository.getNoteById(newNoteId).testIn(backgroundScope).awaitItem()
            // Make sure it's completed
            assertThat(note?.isActive).isFalse()

            // Mark is as active
            notesRepository.activateNote(newNoteId)

            // Verify it's now activated
            assertThat(notesRepository.getNoteById(newNoteId).first()?.isActive).isTrue()
        }
    }

    @Test
    fun clearCompletedNotes() = runTest {
        turbineScope {
            val completedNote = note1.copy(isCompleted = true)
            localDataSource.notes?.clear()
            localDataSource.notes?.addAll(listOf(completedNote.toEntity(), note2.toEntity()))

            notesRepository.clearCompletedNotes()

            val notes = notesRepository.getAllNotes().testIn(backgroundScope).awaitItem()

            assertThat(notes).hasSize(1)
            assertThat(notes).contains(note2)
            assertThat(notes).doesNotContain(completedNote)
        }
    }

    @Test
    fun deleteAllNotes() = runTest {
        turbineScope {
            val initialNotes = notesRepository.getAllNotes().testIn(backgroundScope).awaitItem()

            // Verify notes are returned
            assertThat(initialNotes.size).isEqualTo(1)

            // Delete all notes
            notesRepository.deleteAllNotes()

            // Verify notes are empty now
            val afterDeleteNotes =
                notesRepository.getAllNotes().testIn(backgroundScope).awaitItem()
            assertThat(afterDeleteNotes).isEmpty()
        }
    }

    @Test
    fun deleteANote() = runTest {
        turbineScope {
            val initialNoteSize =
                notesRepository.getAllNotes().testIn(backgroundScope).awaitItem().size
            notesRepository.deleteNote(note3.id)


            val afterDeleteNotes =
                notesRepository.getAllNotes().testIn(backgroundScope).awaitItem()

            assertThat(afterDeleteNotes.size).isEqualTo(initialNoteSize - 1)
            assertThat(afterDeleteNotes).doesNotContain(note3)
        }

    }
}