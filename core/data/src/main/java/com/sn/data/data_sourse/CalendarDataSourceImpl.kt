package com.sn.data.data_sourse

import com.sn.data.ext.toUiModel
import com.sn.domain.model.CalendarUiModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class CalendarDataSourceImpl @Inject constructor() : CalendarDataSource {
    override val today: LocalDate
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    override fun getData(
        startDate: LocalDate,
        lastSelectedDate: LocalDate
    ): CalendarUiModel {
        val endDayOfMonth = startDate.plus(6, DateTimeUnit.MONTH)

        val visibleDates = getDatesBetween(startDate, endDayOfMonth)
        return toUiModel(visibleDates, lastSelectedDate, today)
    }

    override fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val numOfDays = startDate.daysUntil(endDate)
        return (0 until numOfDays).map { startDate.plus(it, DateTimeUnit.DAY) }
    }

}