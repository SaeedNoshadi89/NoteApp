package com.sn.data.repository

import app.cash.turbine.turbineScope
import com.google.common.truth.Truth.assertThat
import com.sn.data.ext.toEntity
import com.sn.domain.gateway.AddNoteRepository
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.gateway.UpdateNoteRepository
import com.sn.domain.model.Note
import com.sn.shared_test.FakeLocalDataSource
import com.sn.shared_test.FakeNoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateNoteRepositoryImplTest {
    private val localNote = Note(
        id = "1",
        title = "Title",
        description = "Description",
        dueDateTime = "123456",
        isCompleted = false
    )
    private val updatedTitle = "updated title"
    private val updatedDesc = "updated desc"
    private val updatedDueDateTime = "1234567"

    private val localNotes = listOf(localNote.toEntity())
    private var testDispatcher = UnconfinedTestDispatcher()
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var noteDao: FakeNoteDao
    private lateinit var updateNoteRepository: UpdateNoteRepository
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
        updateNoteRepository =
            UpdateNoteRepositoryImpl(localDataSource = localDataSource)
        addNoteRepository =
            AddNoteRepositoryImpl(localDataSource = localDataSource, dispatcher = testDispatcher)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getNotesById_UpdateNoteInCache() = runTest {
        turbineScope {
            // Save a note
            val newNoteId =
                addNoteRepository.createNote(
                    title = localNote.title,
                    description = localNote.description ?: "",
                    dueDateTime = localNote.dueDateTime
                )

            // Update the note
            updateNoteRepository.updateNote(
                noteId = newNoteId,
                title = updatedTitle,
                description = updatedDesc,
                dueDateTime = updatedDueDateTime,
                isCompleted = localNote.isCompleted
            )
            // Get a note by id after update
            val noteAfterUpdate =
                notesRepository.getNoteById(newNoteId).testIn(backgroundScope).awaitItem()
            assertThat(noteAfterUpdate?.title).isEqualTo(updatedTitle)
            assertThat(noteAfterUpdate?.description).isEqualTo(updatedDesc)
            assertThat(noteAfterUpdate?.dueDateTime).isEqualTo(updatedDueDateTime)
            assertThat(noteAfterUpdate?.isCompleted).isEqualTo(localNote.isCompleted)
        }
    }

}