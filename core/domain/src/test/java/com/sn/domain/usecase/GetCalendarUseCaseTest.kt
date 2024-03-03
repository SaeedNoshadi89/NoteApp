package com.sn.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.CalendarUiModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCalendarUseCaseTest {

    @Mock
    private lateinit var repository: NotesRepository
    private lateinit var useCase: GetCalendarUseCase

    @Before
    fun setUp() {
        useCase = GetCalendarUseCase(repository)
    }

    @Test
    fun `fetches calendar data successfully`() = runTest {
        // Arrange
        val startDate = LocalDate(2024, 3, 1)
        val lastSelectedDate = LocalDate(2024, 3, 5)
        val expectedCalendarUiModel = createCalendarUiModel()
        `when`(repository.getCalendar(startDate, lastSelectedDate)).thenReturn(flowOf(expectedCalendarUiModel))

        // Act
        val result = useCase(startDate, lastSelectedDate).first()

        // Assert
        assertThat(result).isEqualTo(expectedCalendarUiModel)
        verify(repository).getCalendar(startDate, lastSelectedDate)
    }
    private fun createCalendarUiModel(): CalendarUiModel {
        val selectedDate = LocalDate(2024, 3, 3)
        val visibleDates = listOf(
            // Create Date objects for the visible week, including selected date
            CalendarUiModel.Date(LocalDate(2024, 2, 29), false, false),
            CalendarUiModel.Date(LocalDate(2024, 2, 28), false, false),
            CalendarUiModel.Date(selectedDate, true, false),
            CalendarUiModel.Date(LocalDate(2024, 3, 4), false, false),
            CalendarUiModel.Date(LocalDate(2024, 3, 5), false, false),
            CalendarUiModel.Date(LocalDate(2024, 3, 6), false, false),
            CalendarUiModel.Date(LocalDate(2024, 3, 7), false, false)
        )
        return CalendarUiModel(visibleDates[0], visibleDates)
    }
}
