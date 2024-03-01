package com.sn.data.ext

import com.sn.data.entity.NoteEntity
import com.sn.domain.model.CalendarUiModel
import com.sn.domain.model.Note
import kotlinx.datetime.LocalDate

fun Note.toEntity() = NoteEntity(
    id = id ?: "0",
    title = title,
    description = description,
    isCompleted = isCompleted,
    dueDateTime = dueDateTime,
    category = category
)

fun NoteEntity.toModel() = Note(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    dueDateTime = dueDateTime,
    category = category
)

@JvmName("entityToModel")
fun List<NoteEntity>.toModel() = map(NoteEntity::toModel)


fun toUiModel(
    dateList: List<LocalDate>,
    lastSelectedDate: LocalDate,
    today: LocalDate
): CalendarUiModel {
    return CalendarUiModel(
        selectedDate = toItemUiModel(lastSelectedDate, true, today),
        visibleDates = dateList.map {
            toItemUiModel(it, it == lastSelectedDate, today)
        },
    )
}

fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean, today: LocalDate) = CalendarUiModel.Date(
    isSelected = isSelectedDate,
    isToday = date == today,
    date = date,
)
