package com.sn.designsystem.component.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.sn.core.monthAbbreviations
import com.sn.core.weekAbbreviations
import com.sn.core.withLeadingZero
import com.sn.designsystem.R
import com.sn.domain.model.CalendarUiModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarRow(
    date: CalendarUiModel.Date,
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = R.drawable.calendar),
                contentDescription = "calendar",
                tint = Color.Unspecified
            )
            Text(
                text = if (date.isToday) stringResource(id = R.string.today) else date.date.dayOfMonth.withLeadingZero(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier.basicMarquee(),
                text = "${weekAbbreviations[date.date.dayOfWeek.name]}, ",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.outline,
                )
            )
            Text(
                modifier = Modifier.basicMarquee(),
                text = " ${date.date.dayOfMonth} ${monthAbbreviations[date.date.monthNumber]}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.outline,
                )
            )
        }
    }
}