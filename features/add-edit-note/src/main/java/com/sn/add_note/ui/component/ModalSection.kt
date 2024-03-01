package com.sn.add_note.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sn.add_note.ui.AddNoteUiState
import com.sn.core.formatTimeWithTwoNumber
import com.sn.designsystem.R
import com.sn.designsystem.component.component.SheetRow

@Composable
internal fun ModalSection(
    modifier: Modifier = Modifier,
    uiState: AddNoteUiState.Data,
    onPickDate: () -> Unit,
    onPickTime: () -> Unit
) {
    val formattedTime = "${uiState.dueTime.hour}:${uiState.dueTime.minute}".formatTimeWithTwoNumber()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        SheetRow(
            modifier = modifier,
            title = "${uiState.dueDate ?: "Date is not selected"}",
            trailing = {
                Text(text = formattedTime)
            },
            leading = {
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "calendar"
                )
            },
        )
        Spacer(modifier = Modifier.height(32.dp))
        SheetRow(
            modifier = modifier,
            title = "Pick a date",
            trailing = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "add"
                )
            },
            leading = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_icon),
                    contentDescription = "calendar"
                )
            },
            onClick = onPickDate
        )
        Spacer(modifier = Modifier.height(32.dp))
        SheetRow(
            modifier = modifier,
            title = "Pick a time",
            trailing = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "add"
                )
            },
            leading = {
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "calendar"
                )
            },
            onClick = onPickTime
        )
    }
}

