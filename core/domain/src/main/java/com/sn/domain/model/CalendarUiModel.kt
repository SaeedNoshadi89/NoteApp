package com.sn.domain.model

import com.sn.core.weekAbbreviations
import kotlinx.datetime.LocalDate


data class CalendarUiModel(
    val selectedDate: Date,
    val visibleDates: List<Date>
) {

    val startDate: Date = visibleDates.first()
    val endDate: Date = visibleDates.last()

    data class Date(
        val date: LocalDate,
        val isSelected: Boolean,
        val isToday: Boolean
    ) {

        val day: String = weekAbbreviations[date.dayOfWeek.name] ?: ""
    }

}