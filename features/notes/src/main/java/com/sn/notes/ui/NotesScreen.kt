@file:OptIn(ExperimentalFoundationApi::class)

package com.sn.notes.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sn.designsystem.component.component.SegmentedControl
import com.sn.domain.model.Category
import com.sn.notes.R
import com.sn.notes.ui.component.CalendarUi
import com.sn.notes.ui.component.CategoryComponent
import com.sn.notes.ui.component.Header
import com.sn.notes.ui.component.NoteRow
import com.sn.notes.ui.component.RecentAllNoteHeader
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String) -> Boolean,
    onEditNote: (noteId: String) -> Unit,
    viewModel: NotesViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyCalendarListState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getNotes()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(38.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (val uiState = state) {
            is NotesUiState.Data -> {
                Header(modifier)
                CalendarUi(
                    uiState = uiState,
                    lazyCalendarListState = lazyCalendarListState,
                    onPrevClickListener = {
                        val finalStartDate = uiState.selectedDate?.minus(1, DateTimeUnit.DAY)
                        viewModel.selectDate(finalStartDate)
                    },
                    onNextClickListener = {
                        val finalStartDate = uiState.selectedDate?.plus(1, DateTimeUnit.DAY)
                        viewModel.selectDate(finalStartDate)
                    })
                CategoryComponent(modifier, uiState) {
                    viewModel.selectCategory(it)
                }
                RecentAllNoteHeader(modifier)
                Box(modifier = modifier.weight(1f), contentAlignment = Alignment.Center) {
                    when {
                        uiState.isLoading -> CircularProgressIndicator()
                        uiState.notes.isEmpty() -> Text(
                            modifier = modifier.fillMaxWidth(),
                            text = stringResource(R.string.you_have_not_any_notes),
                            style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
                        )

                        else -> {
                            LazyHorizontalGrid(
                                modifier = modifier.fillMaxSize(),
                                rows = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(uiState.notes, key = { it.id ?: "0" }) { note ->
                                    NoteRow(
                                        modifier = modifier,
                                        note = note,
                                        onEditNote = onEditNote,
                                        onCompleteNote = { viewModel.completeNote(it) },
                                        onActiveNote = { viewModel.activeNote(it) },
                                        onDeleteNote = { viewModel.deleteNote(it) })
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

