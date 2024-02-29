package com.sn.designsystem.component.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sn.designsystem.R
import com.sn.domain.model.CalendarUiModel

@Composable
fun CalendarContent(
    modifier: Modifier = Modifier,
    lazyState: LazyListState = rememberLazyListState(),
    data: CalendarUiModel,
    onPrevClickListener: () -> Unit,
    onNextClickListener: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = modifier.clickable { onPrevClickListener() },
            painter = painterResource(id = R.drawable.arrow_left),
            contentDescription = "arrow left",
            tint = Color.Unspecified
        )

        CarouselRow(list = data.visibleDates, isAutoScrolling = false) { date, _ ->
            CalendarRow(date)
        }
     /*   LazyRow(
            modifier = Modifier
                .width(110.dp),
            state = lazyState,
        ) {
            items(items = data.visibleDates) { date ->
                CalendarRow(date)
            }
        }*/
        Icon(
            modifier = modifier.clickable { onNextClickListener() },
            painter = painterResource(id = R.drawable.arrow_right),
            contentDescription = "arrow right",
            tint = Color.Unspecified
        )
    }
}