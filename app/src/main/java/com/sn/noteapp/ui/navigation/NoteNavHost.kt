package com.sn.noteapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sn.add_note.ui.navigation.addEditNoteScreenGraph
import com.sn.add_note.ui.navigation.navigateToAddOrEditNote
import com.sn.noteapp.ui.NoteAppState
import com.sn.notes.ui.navigation.notesRoute
import com.sn.notes.ui.navigation.notesScreenGraph

@Composable
fun NoteNavHost(
    appState: NoteAppState,
    onShowSnackbar: suspend (String) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = notesRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        notesScreenGraph( onShowSnackbar = onShowSnackbar, onEditNote = {
            navController.navigateToAddOrEditNote(it)
        })

        addEditNoteScreenGraph(onShowSnackbar = onShowSnackbar, onBack = {appState.navBack()})
    }
}