package com.sn.noteapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.sn.add_note.ui.navigation.navigateToAddOrEditNote
import com.sn.designsystem.component.component.Icon
import com.sn.designsystem.component.component.NoteScaffold
import com.sn.noteapp.ui.navigation.DestinationsType
import com.sn.noteapp.ui.navigation.NoteNavHost

@Composable
fun NoteApp(
    windowSizeClass: WindowSizeClass,
    appState: NoteAppState = rememberNoteAppState(
        windowSizeClass = windowSizeClass,
    )
) {
    val snackbarHostState = remember {
        SnackbarHostState()

    }

    NoteScaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .statusBarsPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomAppBarComponent(
                appState = appState
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            NoteNavHost(
                appState = appState,
                onShowSnackbar = { message ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short,
                    ) == SnackbarResult.ActionPerformed
                },
            )
        }
    }

}

@Composable
private fun BottomAppBarComponent(
    appState: NoteAppState,
) {
    appState.currentDestination?.route?.let { route ->
        val navBarVisibility = appState.topLevelDestinations.any {
            appState.currentDestination.isTopLevelDestinationInHierarchy(
                it
            )
        }
        if (navBarVisibility) {
            NoteBottomAppBar(
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination,
                onAdd = { appState.navController.navigateToAddOrEditNote(null) }
            )
        }
    }
}

@Composable
private fun NoteBottomAppBar(
    destinations: List<DestinationsType>,
    onNavigateToDestination: (DestinationsType) -> Unit,
    currentDestination: NavDestination?,
    onAdd: () -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        actions = {
            destinations.forEach { destination ->
                val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
                val textStyle = if (selected) MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                else MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )

                val iconTint =
                    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

                Column(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { onNavigateToDestination(destination) }) {
                        when (val icon = destination.icon) {
                            is Icon.DrawableResourceIcon -> Icon(
                                painter = painterResource(id = icon.id),
                                contentDescription = null,
                                tint = iconTint
                            )

                            is Icon.ImageVectorIcon -> Icon(
                                imageVector = icon.imageVector,
                                contentDescription = null,
                                tint = iconTint
                            )
                        }
                    }
                    Text(
                        text = stringResource(destination.iconTextId),
                        style = textStyle
                    )
                }
            }
        },
        floatingActionButton = {
            Button(onClick = { onAdd() }) {
                Icon(Icons.Filled.Add, "Add note")
            }
        }
    )
}

fun NavDestination?.isTopLevelDestinationInHierarchy(destination: DestinationsType): Boolean {
    val destinationName = when (destination) {
        is DestinationsType.TopLevelDestination -> destination.name
    }
    return this?.hierarchy?.any {
        it.route?.contains(destinationName, true) ?: false
    } ?: false
}
