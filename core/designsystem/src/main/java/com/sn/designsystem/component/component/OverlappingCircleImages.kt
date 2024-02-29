package com.sn.designsystem.component.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun OverlappingCircleImages(
    modifier: Modifier = Modifier,
    image1: Int,
    image2: Int,
) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(id = image1),
            contentDescription = "Image 1",
            modifier = Modifier
                .size(32.dp)
        )
        Image(
            painter = painterResource(id = image2),
            contentDescription = "Image 2",
            modifier = Modifier
                .size(32.dp)
                .offset(x = (-12).dp)
        )
    }
}