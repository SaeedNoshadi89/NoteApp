package com.sn.data.local.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.sn.data.entity.NoteEntity
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDaoTest {


    private lateinit var database: NoteDatabase

    // Ensure that we use a new database for each test.
    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()
    }
    @Test
    fun insertNoteAndGetById() = runTest {
        // GIVEN - insert a note
        val note = NoteEntity(
            title = "title",
            description = "description",
            id = "id",
            isCompleted = false,
            dueDateTime = "1234567",
        )
        database.noteDao().upsertNote(note)

        // WHEN - Get the note by id from the database
        val loaded = database.noteDao().getNoteById(note.id)

        // THEN - The loaded data contains the expected values
        TestCase.assertNotNull(loaded as NoteEntity)
        assertEquals(note.id, loaded.id)
        assertEquals(note.title, loaded.title)
        assertEquals(note.description, loaded.description)
        assertEquals(note.isCompleted, loaded.isCompleted)
        assertEquals(note.dueDateTime, loaded.dueDateTime)
    }

    @Test
    fun insertNoteReplacesOnConflict() = runTest {
        // Given that a note is inserted
        val note = NoteEntity(
            title = "title",
            description = "description",
            id = "id",
            isCompleted = false,
            dueDateTime = "123456",
        )
        database.noteDao().upsertNote(note)

        // When a note with the same id is inserted
        val newNote = NoteEntity(
            title = "new title",
            description = "new description",
            isCompleted = true,
            id = note.id,
            dueDateTime = "12345678"
        )
        database.noteDao().upsertNote(newNote)

        // THEN - The loaded data contains the expected values
        val loaded = database.noteDao().getNoteById(note.id)
        assertEquals(note.id, loaded?.id)
        assertEquals("new title", loaded?.title)
        assertEquals("new description", loaded?.description)
        assertEquals(true, loaded?.isCompleted)
        assertEquals("12345678", loaded?.dueDateTime)
    }

    @Test
    fun insertNoteAndGetNotes() = runTest {
        // GIVEN - insert a note
        val note = NoteEntity(
            title = "title",
            description = "description",
            id = "id",
            isCompleted = false,
            dueDateTime = "123456",
        )
        database.noteDao().upsertNote(note)

        // WHEN - Get notes from the database
        val notes = database.noteDao().getAllNotes(categoryId, selectedDate)

        // THEN - There is only 1 note in the database, and contains the expected values
        assertEquals(1, notes.size)
        assertEquals(notes[0].id, note.id)
        assertEquals(notes[0].title, note.title)
        assertEquals(notes[0].description, note.description)
        assertEquals(notes[0].isCompleted, note.isCompleted)
        assertEquals(notes[0].dueDateTime, note.dueDateTime)
    }

    @Test
    fun updateNoteAndGetById() = runTest {
        // When inserting a note
        val mainNote = NoteEntity(
            title = "title",
            description = "description",
            id = "id",
            isCompleted = false,
            dueDateTime = "123456",
        )

        database.noteDao().upsertNote(mainNote)

        // When the note is updated
        val updatedNote = NoteEntity(
            title = "new title",
            description = "new description",
            isCompleted = true,
            id = mainNote.id,
            dueDateTime = "12345678",
        )
        database.noteDao().upsertNote(updatedNote)

        // THEN - The loaded data contains the expected values
        val loaded = database.noteDao().getNoteById(mainNote.id)
        assertEquals(mainNote.id, loaded?.id)
        assertEquals("new title", loaded?.title)
        assertEquals("new description", loaded?.description)
        assertEquals(true, loaded?.isCompleted)
        assertEquals("12345678", loaded?.dueDateTime)
    }

    @Test
    fun updateCompletedAndGetById() = runTest {
        // When inserting a note
        val note = NoteEntity(
            title = "title",
            description = "description",
            id = "id",
            isCompleted = true,
            dueDateTime = "123456"
        )
        database.noteDao().upsertNote(note)

        // When the note is updated
        database.noteDao().updateCompleted(note.id, false)

        // THEN - The loaded data contains the expected values
        val loaded = database.noteDao().getNoteById(note.id)
        assertEquals(note.id, loaded?.id)
        assertEquals(note.title, loaded?.title)
        assertEquals(note.description, loaded?.description)
        assertEquals(false, loaded?.isCompleted)
        assertEquals("123456", loaded?.dueDateTime)
    }

    @Test
    fun deleteNoteByIdAndGettingNotes() = runTest {
        // Given a note inserted
        val note = NoteEntity(
            title = "title",
            description = "description",
            id = "id",
            isCompleted = false,
            dueDateTime = "123456",
        )
        database.noteDao().upsertNote(note)

        // When deleting a note by id
        database.noteDao().deleteById(note.id)

        // THEN - The list is empty
        val notes = database.noteDao().getAllNotes(categoryId, selectedDate)
        assertEquals(true, notes.isEmpty())
    }

    @Test
    fun deleteNotesAndGettingNotes() = runTest {
        // Given a note inserted
        database.noteDao().upsertNote(
            NoteEntity(
                title = "title",
                description = "description",
                id = "id",
                isCompleted = false,
                dueDateTime = "123456",
            )
        )

        // When deleting all notes
        database.noteDao().deleteAll()

        // THEN - The list is empty
        val notes = database.noteDao().getAllNotes(categoryId, selectedDate)
        assertEquals(true, notes.isEmpty())
    }

    @Test
    fun deleteCompletedNotesAndGettingNotes() = runTest {
        // Given a completed note inserted
        database.noteDao().upsertNote(
            NoteEntity(
                title = "completed",
                description = "note is completed",
                id = "id",
                isCompleted = true,
                dueDateTime = "123456",
            )
        )

        // When deleting completed notes
        database.noteDao().deleteCompleted()

        // THEN - The list is empty
        val notes = database.noteDao().getAllNotes(categoryId, selectedDate)
        assertEquals(true, notes.isEmpty())
    }
}
