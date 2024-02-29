
package com.sn.noteapp.ui.navigation

import com.sn.designsystem.R
import com.sn.designsystem.component.component.Icon
import com.sn.designsystem.component.component.NoteIcons

sealed interface DestinationsType{
    val icon: Icon
    val iconTextId: Int
    val titleTextId: Int

    enum class TopLevelDestination(
        override val icon: Icon,
        override val iconTextId: Int,
        override val titleTextId: Int,
    ): DestinationsType {
        NOTE(
            icon = Icon.DrawableResourceIcon(NoteIcons.Note),
            iconTextId = R.string.note,
            titleTextId = R.string.note,
        ),
        AUDIO(
            icon = Icon.DrawableResourceIcon(NoteIcons.Mic),
            iconTextId = R.string.audio,
            titleTextId = R.string.audio,
        ),
        MESSAGE(
            icon = Icon.DrawableResourceIcon(NoteIcons.Message),
            iconTextId = R.string.message,
            titleTextId = R.string.message,
        ),
        SETTINGS(
            icon = Icon.DrawableResourceIcon(NoteIcons.Settings),
            iconTextId = R.string.settings,
            titleTextId = R.string.settings,
        ),
    }
}