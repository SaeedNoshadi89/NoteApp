package com.sn.domain.usecase

import com.sn.domain.gateway.AddAndEditNoteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateNoteUseCaseTest {

    @Mock
    private lateinit var repository: AddAndEditNoteRepository
    private lateinit var useCase: UpdateNoteUseCase

    @Before
    fun setUp() {
        useCase = UpdateNoteUseCase(repository)
    }

    @Test
    fun `updates a note successfully`() = runTest {
        // Arrange
        val noteId = "123"
        val title = "Updated Title"
        val description = "Updated Description"
        val dueDateTime = "2024-03-03"
        val isCompleted = true
        val category = 1

        `when`(repository.updateNote(
            noteId = noteId,
            title = title,
            description = description,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = category
        )).thenReturn(Unit)

        // Act
        useCase(
            noteId,
            title,
            description,
            dueDateTime,
            isCompleted,
            category
        )

        // Assert
        verify(repository).updateNote(
            noteId = noteId,
            title = title,
            description = description,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = category
        )
    }
}