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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.presentation.common.NoteCard
import com.pproject.sharednotes.presentation.navigation.AppScreens
import com.pproject.sharednotes.presentation.screens.folder.components.FolderHeader
import com.pproject.sharednotes.presentation.screens.folder.components.OnTriggerEditableTitle

@Composable
fun FolderScreen(
    navController: NavController,
    folderViewModel: FolderViewModel = viewModel(),
) {
    val focusRequester = remember { FocusRequester() }
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
                onChangeCanEditTitle = {
                    folderViewModel.updateCanEditTitle(it)
                    folderViewModel.setTitleCursorToEnd()
                },
                canEditTitle = folderViewModel.canEditTitle,
                onAddNewNote = {
                    navController.navigate(
                        AppScreens.NoteScreen.route + "/"
                                + folderViewModel.createNoteInThisFolder()
                    )
                },
                onDeleteFolder = {
                    // TODO: añadir eliminación definitiva de la carpeta
                    navController.popBackStack()
                },
            )
            OnTriggerEditableTitle(
                text = folderViewModel.folder.title,
                onChange = { folderViewModel.updateTitle(it) },
                onDone = { folderViewModel.updateCanEditTitle(false) },
                onEmptyText = { folderViewModel.updateCanEditTitle(true) },
                enabled = folderViewModel.canEditTitle,
                focusRequester = focusRequester,
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(
                    items = folderViewModel.getOrderedNotes(),
                    key = { it }
                ) { noteID ->
                    NoteCard(
                        note = folderViewModel.getNoteFromId(noteID),
                        navController = navController,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
        }
    }
}