package com.sn.add_note.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sn.core.Dispatcher
import com.sn.core.generateDueDateTime
import com.sn.core.getFormattedDateTime
import com.sn.domain.gateway.AddAndEditNoteRepository
import com.sn.domain.model.Category
import com.sn.domain.model.Note
import com.sn.domain.model.TimeModel
import com.sn.domain.usecase.AddNoteUseCase
import com.sn.domain.usecase.GetNoteByIdUseCase
import com.sn.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    @Dispatcher(com.sn.core.Dispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val repository: AddAndEditNoteRepository
) :
    ViewModel() {
    private val noteId = savedStateHandle["idArg"] ?: ""


    private val viewModelState = MutableStateFlow(
        AddNoteViewModelState(
            isLoading = true,
        )
    )

    init {
        if (noteId.isNotEmpty()) {
            getNote()
        }
        getCategories()
    }

    val uiState = viewModelState.map(AddNoteViewModelState::toUiState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = viewModelState.value.toUiState()
    )

    private fun getNote() {
        viewModelScope.launch(ioDispatcher) {
            getNoteByIdUseCase(noteId = noteId).collectLatest { result ->
                result?.let { note ->
                    val formattedDateTime = note.dueDateTime.getFormattedDateTime()
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            note = note,
                            dueDate = formattedDateTime.date,
                            dueTime = TimeModel(
                                formattedDateTime.time.hour,
                                formattedDateTime.time.minute
                            ),
                            selectedCategory = viewModelState.value.categories.find { category -> category.id == note.category },
                            actionMessage = "Updated",
                        )
                    }
                }
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelState.update { it.copy(note = note) }
    }

    fun updateDueDate(date: LocalDate) {
        viewModelState.update { it.copy(dueDate = date) }
    }

    fun updateDueTime(time: TimeModel) {
        viewModelState.update { it.copy(dueTime = time) }
    }

    fun addNote() {
        viewModelScope.launch(ioDispatcher) {
            tryGenerateDueDateTime()
            if (validationData()) return@launch

            val id = if (noteId.isEmpty()) {
                addNoteUseCase(
                    title = viewModelState.value.note.title,
                    description = viewModelState.value.note.description ?: "",
                    dueDateTime = viewModelState.value.note.dueDateTime,
                    isCompleted = false,
                    category = viewModelState.value.selectedCategory?.id ?: 1
                )
            } else {
                updateNoteUseCase(
                    noteId = noteId,
                    title = viewModelState.value.note.title,
                    description = viewModelState.value.note.description ?: "",
                    dueDateTime = viewModelState.value.note.dueDateTime,
                    isCompleted = viewModelState.value.note.isCompleted,
                    category = viewModelState.value.selectedCategory?.id ?: 1
                )
                noteId
            }
            if (id.isNotEmpty()) {
                viewModelState.update {
                    it.copy(
                        isLoading = false,
                        createOrUpdateNoteStatus = true,
                        actionMessage = "Added",
                    )
                }
            }

        }
    }

    private fun validationData(): Boolean {
        if (viewModelState.value.note.title.isEmpty()) {
            viewModelState.update {
                it.copy(
                    error = "title is empty"
                )
            }
            return true
        }
        if (viewModelState.value.note.dueDateTime.isEmpty()) {
            viewModelState.update {
                it.copy(
                    error = "you have to add a date and time"
                )
            }
            return true
        }
        return false
    }

    private fun tryGenerateDueDateTime() {
        viewModelState.value.dueDate?.generateDueDateTime(
            hour = viewModelState.value.dueTime.hour,
            minute = viewModelState.value.dueTime.minute
        )?.let { dueDateTime ->
            viewModelState.update { it.copy(note = viewModelState.value.note.copy(dueDateTime = dueDateTime)) }
        }
    }

    private fun getCategories() {
        viewModelScope.launch(ioDispatcher) {
            repository.getCategories().collectLatest { result ->
                viewModelState.update {
                    it.copy(
                        categories = result,
                        selectedCategory = result[0]
                    )
                }
            }
        }
    }

    fun selectCategory(category: Category) {
        viewModelState.update { it.copy(selectedCategory = category) }
    }

}

sealed interface AddNoteUiState {
    val isLoading: Boolean
    val error: String

    data class Data(
        val note: Note,
        val createOrUpdateNoteStatus: Boolean,
        val dueTime: TimeModel,
        val dueDate: LocalDate?,
        val actionMessage: String,
        val categories: List<Category>,
        val selectedCategory: Category?,
        override val isLoading: Boolean,
        override val error: String
    ) : AddNoteUiState
}

data class AddNoteViewModelState(
    val isLoading: Boolean = false,
    val error: String = "",
    val note: Note = Note(),
    val actionMessage: String = "",
    val dueTime: TimeModel = TimeModel(),
    val dueDate: LocalDate? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val createOrUpdateNoteStatus: Boolean = false,
) {
    fun toUiState(): AddNoteUiState =
        AddNoteUiState.Data(
            isLoading = isLoading,
            error = error,
            note = note,
            dueTime = dueTime,
            dueDate = dueDate,
            actionMessage = actionMessage,
            createOrUpdateNoteStatus = createOrUpdateNoteStatus,
            categories = categories,
            selectedCategory = selectedCategory
        )
}