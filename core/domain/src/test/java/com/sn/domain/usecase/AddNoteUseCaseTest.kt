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
class AddNoteUseCaseTest {

    @Mock
    private lateinit var repository: AddAndEditNoteRepository
    private lateinit var useCase: AddNoteUseCase

    @Before
    fun setUp() {
        useCase = AddNoteUseCase(repository)
    }

    @Test
    fun `adds a note successfully`() = runTest {
        // Arrange
        val title = "New Note"
        val description = "Note description"
        val dueDateTime = "1732414"
        val isCompleted = false
        val category = 2

        `when`(repository.createNote(
            title = title,
            description = description,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = category
        )).thenReturn("1212")

        // Act
        useCase(
            title,
            description,
            dueDateTime,
            isCompleted,
            category
        )

        // Assert
        verify(repository).createNote(
            title = title,
            description = description,
            dueDateTime = dueDateTime,
            isCompleted = isCompleted,
            category = category
        )
    }
}
