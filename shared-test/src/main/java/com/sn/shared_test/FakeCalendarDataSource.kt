package com.sn.shared_test

import com.sn.data.data_sourse.CalendarDataSource
import com.sn.data.ext.toUiModel
import com.sn.domain.model.CalendarUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FakeCalendarDataSource : CalendarDataSource {

    private val _today = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    override val today: LocalDate
        get() = _today.value

    private var _visibleDates: List<LocalDate> = emptyList()
    val visibleDates: List<LocalDate>
        get() = _visibleDates

    fun setFakeDates(dates: List<LocalDate>) {
        _visibleDates = dates
    }

    override fun getData(
        startDate: LocalDate,
        lastSelectedDate: LocalDate
    ): CalendarUiModel = toUiModel(visibleDates, lastSelectedDate, today)

    override fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> =
        visibleDates.filter { it in startDate..endDate }
}