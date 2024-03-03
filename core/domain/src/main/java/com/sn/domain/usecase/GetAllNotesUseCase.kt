package com.sn.domain.usecase

import com.sn.core.getFormattedDateTime
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.Note
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@OptIn(FlowPreview::class)
class GetAllNotesUseCase @Inject constructor(private val repository: NotesRepository) {
    operator fun invoke(categoryId: Int?, selectedDate: LocalDate?): Flow<List<Note>> =
        repository.getAllNotes(categoryId, selectedDate).flatMapConcat { notes ->
            val map = if (categoryId == 1) {
                notes
            } else {
                notes.filter { isMatchingDate(it.dueDateTime, selectedDate) }

            }
            flowOf(map)
        }

    private fun isMatchingDate(dueDateTime: String, selectedDate: LocalDate?): Boolean {
        if (selectedDate == null || dueDateTime.isEmpty()) return true

        val date = dueDateTime.getFormattedDateTime().date
        return date.toString() == selectedDate.toString()
    }

}