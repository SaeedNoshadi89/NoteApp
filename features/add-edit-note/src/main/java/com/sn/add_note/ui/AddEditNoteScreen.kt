@file:OptIn(ExperimentalMaterial3Api::class)

package com.sn.add_note.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sn.designsystem.component.component.DatePickerInDatePickerDialog
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val openDatePicker = remember {
        mutableStateOf(false)
    }

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
            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                CenterAlignedTopAppBar(
                    title = { Text(text = uiState.title) },
                    navigationIcon = {
                        Icon(
                            modifier = modifier.clickable { onBack() },
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                )
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = uiState.note.title,
                        onValueChange = { viewModel.updateNote(uiState.note.copy(title = it)) },
                        placeholder = {
                            Text(
                                text = "Enter title"
                            )
                        })
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = uiState.note.description ?: "",
                        onValueChange = { viewModel.updateNote(uiState.note.copy(description = it)) },
                        placeholder = {
                            Text(
                                text = "Enter description"
                            )
                        })
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        Button(onClick = { openDatePicker.value = true }) {
                            Text(
                                text = "Select date ${uiState.dueDate ?: ""}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 17.sp
                                ),
                            )
                        }
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
                        TimePickerWithDialog(timePickerState = {
                            viewModel.updateDueTime(time = TimeModel(it.hour, it.minute))
                        })

                    }
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 30.dp), contentAlignment = Alignment.Center
                    ) {
                        Button(modifier = modifier.fillMaxWidth(), onClick = {
                            viewModel.addNote()
                        }) {
                            Text(text = uiState.buttonText)
                        }
                    }
                }
            }

        }
    }
}