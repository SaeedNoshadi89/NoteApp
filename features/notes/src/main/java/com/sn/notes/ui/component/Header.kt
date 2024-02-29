package com.sn.notes.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun Header(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = modifier.size(40.dp),
                painter = painterResource(
                    id =
                    com.sn.designsystem.R.drawable.profile
                ),
                contentDescription = "profile"
            )
            Text(
                text = "Saeed Noshadi",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            Icon(
                painter = painterResource(id = com.sn.designsystem.R.drawable.scroll),
                contentDescription = "scroll"
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = com.sn.designsystem.R.drawable.direct_inbox),
                contentDescription = "direct inbox",
                tint = Color.Unspecified
            )
            Icon(
                painter = painterResource(id = com.sn.designsystem.R.drawable.notification_status),
                contentDescription = "notification status",
                tint = Color.Unspecified
            )
        }
    }
}