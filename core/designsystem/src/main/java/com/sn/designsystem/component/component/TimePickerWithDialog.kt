package com.sn.designsystem.component.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sn.core.formatTimeWithTwoNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerWithDialog(
    modifier: Modifier = Modifier,
    timePickerState: (TimePickerState) -> Unit,
) {

    var showDialog by remember { mutableStateOf(false) }
    val timeState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = 0
    )
    val formattedTime = "${timeState.hour}:${timeState.minute}".formatTimeWithTwoNumber()
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
        ) {
            Column(
                modifier = modifier
                    .background(color = Color.White)
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = timeState)
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showDialog = false }) {
                        Text(text = "Dismiss")
                    }
                    TextButton(onClick = {
                        showDialog = false
                        timePickerState(timeState)
                    }) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { showDialog = true }) {
            Text(
                text = "Select time $formattedTime",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 17.sp
                ),
            )
        }
    }
}