package com.sn.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllNotesUseCaseTest {

    @Mock
    private lateinit var repository: NotesRepository
    private lateinit var useCase: GetAllNotesUseCase

    @Before
    fun setUp() {
        useCase = GetAllNotesUseCase(repository)
    }

    @Test
    fun `gets all notes by category`() = runTest {
        // Arrange
        val categoryId = 1
        val notes = listOf(
            Note(title = "Note 1", category = 1),
            Note(title = "Note 2", category = 2)
        )
        `when`(repository.getAllNotes(categoryId, null)).thenReturn(flowOf(notes))

        // Act
        val result = useCase(categoryId, null).first()

        // Assert
        assertThat(result).containsExactlyElementsIn(notes)
        verify(repository).getAllNotes(categoryId, null)
    }

    @Test
    fun `gets notes by selected date`() = runTest {
        // Arrange
        val selectedDate = LocalDate(1970, 7, 20)
        val notes = listOf(
            Note(title = "Note 1", dueDateTime = "17323520000"),
            Note(title = "Note 2", dueDateTime = "17753520000")
        )
        `when`(repository.getAllNotes(null, selectedDate)).thenReturn(flowOf(notes))

        // Act
        val result = useCase(null, selectedDate).first()

        // Assert
        assertThat(result).containsExactly(notes[0])
        verify(repository).getAllNotes(null, selectedDate)
    }

    @Test
    fun `handles error from repository`() = runTest {
        // Arrange
        val errorMessage = "Error fetching notes"
        `when`(repository.getAllNotes(any(), any())).thenThrow(RuntimeException(errorMessage))

        // Assert
        assertThrows(RuntimeException::class.java) {
            useCase(2, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
        }
        verify(repository).getAllNotes(2, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    }
}
