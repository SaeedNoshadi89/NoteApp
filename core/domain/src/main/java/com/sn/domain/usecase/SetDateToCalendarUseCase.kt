package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.CalendarUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class SetDateToCalendarUseCase @Inject constructor(private val repository: NotesRepository) {
    operator fun invoke(
        date: LocalDate,
    ): Flow<CalendarUiModel> = repository.setDateToCalendar(date)

}