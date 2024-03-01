package com.sn.add_note.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
internal fun Header(modifier: Modifier = Modifier,  onBack: () -> Unit, onSave: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = modifier.clickable { onBack() },
            painter = painterResource(id = com.sn.designsystem.R.drawable.back),
            contentDescription = "back"
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = modifier.clickable { onSave() },
                painter = painterResource(id = com.sn.designsystem.R.drawable.archive_circle),
                contentDescription = "archive circle",
                tint = Color.Unspecified
            )
            Icon(
                painter = painterResource(id = com.sn.designsystem.R.drawable.direct_inbox_circle),
                contentDescription = "direct inbox circle",
                tint = Color.Unspecified
            )
        }
    }
}