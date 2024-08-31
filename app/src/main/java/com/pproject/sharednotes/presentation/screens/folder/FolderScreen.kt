package com.pproject.sharednotes.presentation.screens.folder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.data.local.entity.Folder
import com.pproject.sharednotes.data.local.entity.FolderWithNotes
import com.pproject.sharednotes.presentation.common.NoteCard
import com.pproject.sharednotes.presentation.screens.folder.components.FolderHeader
import com.pproject.sharednotes.presentation.screens.folder.components.OnTriggerEditableTitle

@Composable
fun FolderScreen(
    navController: NavController,
    folderViewModel: FolderViewModel = viewModel(factory = FolderViewModel.Factory),
) {
    val focusRequester = remember { FocusRequester() }
    val folder by folderViewModel.folderWithNotes.observeAsState(
        FolderWithNotes(
            Folder(userName = ""),
            emptyList()
        )
    )
    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            FolderHeader(
                onClickBack = { navController.popBackStack() },
                onChangeCanEditTitle = { folderViewModel.updateCanEditTitle(it) },
                canEditTitle = folderViewModel.uiState.canEditTitle,
                onAddNewNote = { folderViewModel.createNoteInThisFolder(navController) },
                onDeleteFolder = {
                    folderViewModel.deleteFolder()
                    navController.popBackStack()
                },
            )
            OnTriggerEditableTitle(
                text = folderViewModel.uiState.title,
                onChange = { folderViewModel.updateTitle(it) },
                onDone = { folderViewModel.updateCanEditTitle(false) },
                isNewFolder = folderViewModel.isNewFolder,
                onNewFolder = {
                    folderViewModel.updateCanEditTitle(true)
                    folderViewModel.updateIsNewFolder(false)
                },
                enabled = folderViewModel.uiState.canEditTitle,
                focusRequester = focusRequester
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(
                    items = folder.notes,
                ) { note ->
                    NoteCard(
                        note = note,
                        activeUser = folderViewModel.activeUser,
                        navController = navController,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
        }
    }
    LaunchedEffect(folder) {
        if (folderViewModel.uiState.isEmpty()) {
            folderViewModel.loadFolder()
        }
    }
}