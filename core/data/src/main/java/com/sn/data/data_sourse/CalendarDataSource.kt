package com.sn.data.data_sourse

import com.sn.domain.model.CalendarUiModel
import kotlinx.datetime.LocalDate

interface CalendarDataSource {
    val today: LocalDate

    fun getData(startDate: LocalDate = today, lastSelectedDate: LocalDate): CalendarUiModel

    fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate>
}