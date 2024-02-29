package com.sn.notes.ui.component

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
import com.sn.designsystem.component.component.SegmentedControl
import com.sn.domain.model.Category
import com.sn.notes.ui.NotesUiState

@Composable
internal fun CategoryComponent(
    modifier: Modifier,
    uiState: NotesUiState.Data,
    onSelectCategory: (Category) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SegmentedControl(
            modifier = modifier.weight(1f),
            items = uiState.categories,
            hasDivider = false,
            height = 38.dp,
            defaultSelectedItemIndex = 0
        ) { item ->
            onSelectCategory(item)
        }
        Icon(
            painter = painterResource(id = com.sn.designsystem.R.drawable.archive_minus),
            contentDescription = "save",
            tint = Color.Unspecified
        )
    }
}