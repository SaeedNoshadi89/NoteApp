package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteNoteUseCaseTest {

    @Mock
    private lateinit var repository: NotesRepository
    private lateinit var useCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        useCase = DeleteNoteUseCase(repository)
    }

    @Test
    fun `deletes a note successfully`() = runTest {
        // Arrange
        val noteId = "123"
        `when`(repository.deleteNote(noteId)).thenReturn(Unit)

        // Act
        useCase(noteId)

        // Assert
        verify(repository).deleteNote(noteId)
    }

    @Test
    fun `handles error from repository`() = runTest {
        // Arrange
        val noteId = "456"
        val errorMessage = "Error deleting note"
        `when`(repository.deleteNote(noteId)).thenThrow(RuntimeException(errorMessage))

        // Assert
        assertThrows(IllegalStateException::class.java) {
            runTest {
                useCase(noteId)
            }
        }
        verify(repository).deleteNote(noteId)
    }

    @Test
    fun `throws exception for invalid note ID`() = runTest {
        // Arrange
        val invalidNoteId = ""

        // Assert
        assertThrows(IllegalStateException::class.java) {
            runTest {
                useCase(invalidNoteId)
            }
        }
        verifyNoInteractions(repository)
    }
}
