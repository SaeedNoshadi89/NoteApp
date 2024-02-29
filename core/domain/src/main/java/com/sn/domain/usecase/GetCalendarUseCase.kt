package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.CalendarUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class GetCalendarUseCase @Inject constructor(private val repository: NotesRepository) {
    operator fun invoke(
        startDate: LocalDate,
        lastSelectedDate: LocalDate,
    ): Flow<CalendarUiModel> = repository.getCalendar(startDate, lastSelectedDate)

}