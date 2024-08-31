package com.pproject.sharednotes.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.presentation.screens.home.components.FolderHorizontalGrid
import com.pproject.sharednotes.presentation.screens.home.components.HomeAddButton
import com.pproject.sharednotes.presentation.screens.home.components.HomeHeader

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
) {
    val folders by homeViewModel.foldersWithNotes.observeAsState(emptyList())
    val notesWithoutFolder by homeViewModel.notesWithoutFolder.observeAsState(emptyList())
    val notificationPairs by homeViewModel.getNotificationPairs(navController.context)
        .observeAsState(emptyList())
    Surface {
        Scaffold(
            topBar = {
                HomeHeader(
                    notificationPairs = notificationPairs,
                    onAcceptNotification = { homeViewModel.interactNotification(it, true) },
                    onDeleteNotification = { homeViewModel.interactNotification(it, false) },
                    onLogOut = {
                        homeViewModel.logOut(navController)
                    }
                )
            },
            floatingActionButton = {
                HomeAddButton(
                    onCreateFolder = { homeViewModel.createNewFolder(navController) },
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                LazyColumn {
                    items(
                        items = folders,
                    ) { thisFolder ->
                        FolderHorizontalGrid(
                            folder = thisFolder.folder,
                            notes = thisFolder.notes,
                            activeUser = homeViewModel.activeUser,
                            navController = navController,
                            onCreateNote = {
                                homeViewModel.createNewNote(navController, it)
                            },
                            onDeleteFolder = { homeViewModel.deleteFolder(it) },
                            modifier = Modifier.padding(10.dp),
                        )
                    }
                    item {
                        FolderHorizontalGrid(
                            folder = null,
                            notes = notesWithoutFolder,
                            activeUser = homeViewModel.activeUser,
                            navController = navController,
                            onCreateNote = {
                                homeViewModel.createNewNote(navController, it)
                            },
                        )
                    }
                }
            }
        }
    }
}

