package com.sn.designsystem.component.component

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.sn.designsystem.R


object NoteIcons {
    ///////////////******Painters******/////////////////
    val Note = R.drawable.note_icon
    val Mic = R.drawable.microphone
    val Message = R.drawable.message
    val Settings = R.drawable.setting
    val Back = R.drawable.back

}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}