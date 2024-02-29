package com.sn.notes.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.sn.notes.ui.NotesScreen

const val notesRoute = "note_route"

fun NavController.navigateToNotes( navOptions: NavOptions? = null){
    navigate(notesRoute, navOptions)
}

fun NavGraphBuilder.notesScreenGraph(
    onShowSnackbar: suspend (String) -> Boolean,
    onEditNote: (noteId: String) -> Unit,
) {
    composable(
        route = notesRoute
    ) {
        NotesScreen(onShowSnackbar = {onShowSnackbar(it)}, onEditNote = {onEditNote(it)})
    }
}