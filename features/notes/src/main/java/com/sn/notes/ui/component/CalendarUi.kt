package com.sn.notes.ui.component

import androidx.compose.runtime.Composable
import com.sn.designsystem.component.component.CalendarContent
import com.sn.notes.ui.NotesUiState

@Composable
internal fun CalendarUi(
    uiState: NotesUiState,
    onPrevClickListener: () -> Unit,
    onNextClickListener: () -> Unit,
) {
    uiState.calendar?.let { calendar ->
        CalendarContent(
            data = calendar,
            onPrevClickListener = {onPrevClickListener()},
            onNextClickListener = {onNextClickListener()}
        )
    }
}
