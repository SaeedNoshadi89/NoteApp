@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.sn.add_note.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sn.add_note.ui.component.CategorySection
import com.sn.add_note.ui.component.Header
import com.sn.add_note.ui.component.ModalSection
import com.sn.designsystem.component.component.DatePickerInDatePickerDialog
import com.sn.designsystem.component.component.NoteBasicTextField
import com.sn.designsystem.component.component.TimePickerWithDialog
import com.sn.domain.model.TimeModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun AddEditNoteScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onShowSnackbar: suspend (String) -> Boolean,
    viewModel: AddNoteViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val openDatePicker = remember {
        mutableStateOf(false)
    }
    var showTimePicker = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(true) }
    when (val uiState = state) {
        is AddNoteUiState.Data -> {
            LaunchedEffect(key1 = uiState.createOrUpdateNoteStatus) {
                if (uiState.createOrUpdateNoteStatus) {
                    scope.launch { onShowSnackbar(uiState.actionMessage) }
                    onBack()
                }
            }
            LaunchedEffect(key1 = uiState.error) {
                if (uiState.error.isNotEmpty()) {
                    scope.launch {
                        onShowSnackbar(uiState.error)
                    }
                }
            }
            BoxWithConstraints {
                val maxHeight = maxHeight
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    Header(onBack = onBack, onSave = { viewModel.addNote() })
                    CategorySection(uiState = uiState) {
                        viewModel.selectCategory(it)
                    }

                    Surface(
                        modifier = modifier.weight(1f),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        color = MaterialTheme.colorScheme.background,
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            NoteBasicTextField(
                                value = uiState.note.title,
                                updateTextValue = { viewModel.updateNote(uiState.note.copy(title = it)) },
                                textStyle = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                                placeholder = "Enter title",
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            )

                            NoteBasicTextField(
                                value = uiState.note.description ?: "",
                                updateTextValue = {
                                    viewModel.updateNote(
                                        uiState.note.copy(
                                            description = it
                                        )
                                    )
                                },
                                textStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                                placeholder = "Enter description",
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            )

                            Column(
                                modifier = modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                DatePickerInDatePickerDialog(
                                    openDialog = openDatePicker.value,
                                    onDismiss = { openDatePicker.value = false },
                                    selectedDate = { date ->
                                        date?.let {
                                            if (Instant.fromEpochMilliseconds(it)
                                                    .toLocalDateTime(TimeZone.currentSystemDefault()).date < Clock.System.now()
                                                    .toLocalDateTime(
                                                        TimeZone.currentSystemDefault()
                                                    ).date
                                            ) {
                                                scope.launch {
                                                    onShowSnackbar("Date is not valid")
                                                }
                                                return@let
                                            }
                                            viewModel.updateDueDate(
                                                Instant.fromEpochMilliseconds(it)
                                                    .toLocalDateTime(
                                                        TimeZone.currentSystemDefault()
                                                    ).date
                                            )
                                            scope.launch {
                                                onShowSnackbar("Date is selected")
                                            }
                                        }
                                    })
                            }
                            Column(
                                modifier = modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                TimePickerWithDialog(
                                    showTimePicker = showTimePicker.value,
                                    timePickerState = {
                                        viewModel.updateDueTime(
                                            time = TimeModel(
                                                it.hour,
                                                it.minute
                                            )
                                        )
                                    },
                                    onDismiss = { showTimePicker.value = false })

                            }
                        }
                    }


                }
                if (showBottomSheet) {
                    ModalBottomSheet(
                        modifier = modifier
                            .heightIn(max = maxHeight.div(3f))
                            .safeDrawingPadding(),
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        sheetState = sheetState,
                    ) {
                        ModalSection(
                            uiState = uiState,
                            onPickDate = { openDatePicker.value = true },
                            onPickTime = { showTimePicker.value = true })
                    }
                }
            }
        }
    }
}
