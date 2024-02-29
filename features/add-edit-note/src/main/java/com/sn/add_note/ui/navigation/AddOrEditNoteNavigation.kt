package com.sn.add_note.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sn.add_note.ui.AddEditNoteScreen

const val addOrEditNoteRoute = "add_edit_route"
const val idArg = "idArg"

fun NavController.navigateToAddOrEditNote(noteId: String?, navOptions: NavOptions? = null) {
    navigate("$addOrEditNoteRoute/$noteId", navOptions)
}

fun NavGraphBuilder.addEditNoteScreenGraph(
    onShowSnackbar: suspend (String) -> Boolean,
    onBack: () -> Unit,
) {
    composable(
        route = "$addOrEditNoteRoute/{$idArg}",
        arguments = listOf(
            navArgument(idArg) { type = NavType.StringType; nullable = true },
        )
    ) {
        AddEditNoteScreen(onShowSnackbar = { onShowSnackbar(it) }, onBack = onBack)
    }
}