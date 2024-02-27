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
        isCompleted = false
    )

    private val localNotes = listOf(note.toEntity())
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
    fun addANote_getNotesCache() = runTest {
        turbineScope {
            // get all notes before create a new
            val oldNotes = notesRepository.getAllNotes().testIn(backgroundScope).awaitItem()
            assertThat(oldNotes.size).isEqualTo(1)
            //Save a note
            addNoteRepository.createNote(
                title = "new title",
                description = "new description",
                dueDateTime = "1234567"
            )

            val newNotes = notesRepository.getAllNotes().testIn(backgroundScope).awaitItem()
            assertThat(newNotes.size).isEqualTo(2)
        }
    }
}