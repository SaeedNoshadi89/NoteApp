package com.sn.domain.usecase

import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(private val repository: NotesRepository) {
    operator fun invoke(categoryId: Int?, selectedDate: LocalDate?): Flow<List<Note>> =
        repository.getAllNotes(categoryId).map {list ->
            list.filter {
              val date = Instant.fromEpochMilliseconds(it.dueDateTime.toLong())
                    .toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    ).date.toString()
                date == selectedDate.toString()
            }.map {
                it.copy(
                    dueDateTime = Instant.fromEpochMilliseconds(it.dueDateTime.toLong())
                        .toLocalDateTime(
                            TimeZone.currentSystemDefault()
                        ).toString()
                )
            }
        }
}