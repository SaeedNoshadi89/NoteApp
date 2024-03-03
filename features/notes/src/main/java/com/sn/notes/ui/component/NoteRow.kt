package com.sn.notes.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sn.designsystem.R
import com.sn.designsystem.component.component.OverlappingCircleImages
import com.sn.domain.model.Note


@Composable
internal fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    onEditNote: (noteId: String) -> Unit,
    onCompleteNote: (String?) -> Unit,
    onActiveNote: (String?) -> Unit,
    onDeleteNote: (String?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier
            .width(269.dp)
            .height(179.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 4.dp
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Column {
                    Icon(
                        modifier = modifier.clickable { expanded = !expanded },
                        painter = painterResource(id = R.drawable.rectangle),
                        contentDescription = "rectangle",
                        tint = Color.Unspecified
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete", fontWeight = FontWeight.Bold) },
                            onClick = { onDeleteNote(note.id) },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }
            }

            Text(
                modifier = modifier.weight(1f),
                text = note.description ?: "There is no description",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelLarge
            )

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OverlappingCircleImages(
                    image1 = R.drawable.profile,
                    image2 = R.drawable.profile
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = modifier
                            .size(16.dp)
                            .clickable {
                                if (note.isCompleted) onActiveNote(note.id) else onCompleteNote(note.id)
                            },
                        painter = painterResource(id = R.drawable.archive_minus),
                        contentDescription = "save",
                        tint = if (note.isCompleted) Color.Red else Color.Unspecified
                    )
                    Icon(
                        modifier = modifier.clickable { note.id?.let { onEditNote(it) } },
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "edit",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}