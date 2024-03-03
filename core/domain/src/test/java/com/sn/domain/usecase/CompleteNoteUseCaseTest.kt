package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompleteNoteUseCaseTest {

    @Mock
    private lateinit var repository: NotesRepository
    private lateinit var useCase: CompleteNoteUseCase

    @Before
    fun setUp() {
        repository = Mockito.mock(NotesRepository::class.java)
        useCase = CompleteNoteUseCase(repository)
    }

    @Test
    fun `completes a note successfully`() = runTest{
        // Arrange
        val noteId = "123"
        `when`(repository.completeNote(noteId)).thenReturn(Unit)

        // Act
        useCase(noteId)

        // Assert
        verify(repository).completeNote(noteId)
    }
}