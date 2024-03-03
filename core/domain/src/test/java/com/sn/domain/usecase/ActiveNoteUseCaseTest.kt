package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ActiveNoteUseCaseTest {

    @Mock
    private lateinit var repository: NotesRepository
    private lateinit var useCase: ActiveNoteUseCase

    @Before
    fun setUp() {
        useCase = ActiveNoteUseCase(repository)
    }

    @Test
    fun `activates a note successfully`() = runTest {
        // Arrange
        val noteId = "123"

        `when`(repository.activateNote(noteId)).thenReturn(Unit)

        // Act
        useCase(noteId)

        // Assert
        verify(repository).activateNote(noteId)
    }
}
