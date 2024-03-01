package com.sn.notes.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sn.core.Dispatcher
import com.sn.core.convertToUtc
import com.sn.domain.gateway.NotesRepository
import com.sn.domain.model.CalendarUiModel
import com.sn.domain.model.Category
import com.sn.domain.model.Note
import com.sn.domain.usecase.ActiveNoteUseCase
import com.sn.domain.usecase.CompleteNoteUseCase
import com.sn.domain.usecase.DeleteNoteUseCase
import com.sn.domain.usecase.GetAllNotesUseCase
import com.sn.domain.usecase.GetCalendarUseCase
import com.sn.domain.usecase.SetDateToCalendarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    @Dispatcher(com.sn.core.Dispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher,
    private val getCalendarUseCase: GetCalendarUseCase,
    private val setDateToCalendarUseCase: SetDateToCalendarUseCase,
    private val completeNoteUseCase: CompleteNoteUseCase,
    private val activeNoteUseCase: ActiveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val repository: NotesRepository
) :
    ViewModel() {

    private val viewModelState = MutableStateFlow(
        NotesViewModelState(
            isLoading = true,
        )
    )

    init {
        getCalendar()
        selectDate()
        getCategories()
    }

    val uiState = viewModelState.map(NotesViewModelState::toUiState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = viewModelState.value.toUiState()
    )

    fun getNotes() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch(ioDispatcher) {
            getAllNotesUseCase(viewModelState.value.selectedCategory?.id, viewModelState.value.selectedDate)
                .collectLatest { notes ->
                viewModelState.update { it.copy(isLoading = false, notes = notes) }
            }
        }
    }

    private fun getCalendar(
        startDate: LocalDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date,
        lastSelectedDate: LocalDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    ) {
        viewModelScope.launch(ioDispatcher) {
            getCalendarUseCase(startDate, lastSelectedDate).collectLatest { calendar ->
                viewModelState.update { it.copy(calendar = calendar) }
            }
        }
    }

    fun selectDate(
        date: LocalDate? = Clock.System.now().convertToUtc()
            ?.toLocalDateTime(TimeZone.currentSystemDefault())?.date
    ) {
        viewModelScope.launch(ioDispatcher) {
            date?.let { selectedDate ->
                viewModelState.update { it.copy(selectedDate = selectedDate) }
                setDateToCalendarUseCase(selectedDate).collectLatest { calendar ->
                    getCalendar(selectedDate)
                    getNotes()
                }
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch(ioDispatcher) {
            repository.getCategories().collectLatest { result ->
                viewModelState.update { it.copy(categories = result, selectedCategory = result[0]) }
            }
        }
    }

    fun selectCategory(category: Category) {
        viewModelState.update { it.copy(selectedCategory = category) }
        getNotes()
    }

    fun completeNote(noteId: String?){
        viewModelScope.launch(ioDispatcher) {
            noteId?.let {id ->
                completeNoteUseCase(
                    noteId = id,
                )
            }
            getNotes()
        }
    }

    fun activeNote(noteId: String?){
        viewModelScope.launch(ioDispatcher) {
            noteId?.let {id ->
                activeNoteUseCase(
                    noteId = id,
                )
            }
            getNotes()
        }
    }

    fun deleteNote(noteId: String?){
        viewModelScope.launch(ioDispatcher) {
            noteId?.let {id ->
                deleteNoteUseCase(
                    noteId = id,
                )
            }
            getNotes()
        }
    }

}

sealed interface NotesUiState {
    val isLoading: Boolean
    val error: String

    @Stable
    data class Data(
        val notes: List<Note>,
        val calendar: CalendarUiModel? = null,
        val selectedDate: LocalDate? = null,
        val categories: List<Category>,
        val selectedCategory: Category?,
        override val isLoading: Boolean,
        override val error: String
    ) : NotesUiState
}

data class NotesViewModelState(
    val isLoading: Boolean = false,
    val error: String = "",
    val calendar: CalendarUiModel? = null,
    val selectedDate: LocalDate? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val notes: List<Note> = emptyList(),
) {
    fun toUiState(): NotesUiState =
        NotesUiState.Data(
            isLoading = isLoading,
            error = error,
            notes = notes,
            calendar = calendar,
            selectedDate = selectedDate,
            categories = categories,
            selectedCategory = selectedCategory
        )
}