package com.sn.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.sn.domain.gateway.AddAndEditNoteRepository
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetNoteByIdUseCaseTest {

    @Mock
    private lateinit var repository: AddAndEditNoteRepository
    private lateinit var useCase: GetNoteByIdUseCase

    @Before
    fun setUp() {
        useCase = GetNoteByIdUseCase(repository)
    }

    @Test
    fun `fetches a note successfully`() = runTest {
        // Arrange
        val noteId = "123"
        val expectedNote = Note(title = "Test Note", description = "This is a test note")
        `when`(repository.getNoteById(noteId)).thenReturn(flowOf(expectedNote))

        // Act
        val result = useCase(noteId).first()

        // Assert
        assertThat(result).isEqualTo(expectedNote)
        verify(repository).getNoteById(noteId)
    }

    @Test
    fun `returns null when note not found`() = runTest {
        // Arrange
        val noteId = "456"
        `when`(repository.getNoteById(noteId)).thenReturn(flowOf(null))

        // Act
        val result = useCase(noteId).first()

        // Assert
        assertThat(result).isNull()
        verify(repository).getNoteById(noteId)
    }

}
