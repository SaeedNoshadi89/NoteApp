package com.sn.noteapp.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.sn.noteapp.ui.navigation.DestinationsType
import com.sn.notes.ui.navigation.navigateToNotes
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberNoteAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): NoteAppState {
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
    ) {
        NoteAppState(
            navController,
        )
    }
}

@Stable
class NoteAppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val topLevelDestinations: List<DestinationsType.TopLevelDestination> =
        DestinationsType.TopLevelDestination.values().asList()

    fun navBack() {
        when (navController.currentDestination?.route) {
            /*!notesRoute -> navigateToTopLevelDestination(
                DestinationsType.TopLevelDestination.NOTE
            )*/

            else -> navController.popBackStack()
        }
    }

    fun navigateToTopLevelDestination(topLevelDestination: DestinationsType) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            DestinationsType.TopLevelDestination.NOTE -> {
                navController.navigateToNotes(
                    navOptions = topLevelNavOptions
                )
            }

            DestinationsType.TopLevelDestination.AUDIO -> {

            }

            DestinationsType.TopLevelDestination.MESSAGE -> {

            }

            DestinationsType.TopLevelDestination.SETTINGS -> {

            }
        }
    }
}

