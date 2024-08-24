package com.pproject.sharednotes.presentation.screens.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.NoteWithFolders
import com.pproject.sharednotes.presentation.screens.note.components.EditableTitle
import com.pproject.sharednotes.presentation.screens.note.components.NoteHeader
import com.pproject.sharednotes.presentation.screens.note.components.Section
import com.pproject.sharednotes.presentation.screens.note.components.SectionsField

@Composable
fun NoteScreen(
    navController: NavController,
    noteViewModel: NoteViewModel = viewModel(factory = NoteViewModel.Factory),
) {
    val users by noteViewModel.users.observeAsState(emptyList())
    val note by noteViewModel.note.observeAsState(NoteWithFolders(Note(), emptyList()))
    val folderPairNames by noteViewModel.allPairNameFolders.observeAsState(emptyList())
    Surface {
        Scaffold(
            topBar = {
                NoteHeader(
                    onClickBack = { navController.popBackStack() },
                    note = note.note,
                    users = users,
                    isPinnedNote = noteViewModel.uiState.pinned,
                    selectedFolder = noteViewModel.getSelectedFolderId(),
                    onChangeFolder = { old, new -> noteViewModel.updateFolder(old, new) },
                    folderList = folderPairNames,
                    onAddCollaborator = { noteViewModel.inviteCollaborator(it) },
                    onDeleteCollaborator = { noteViewModel.deleteCollaborator(it) },
                    onChangePinned = { noteViewModel.updatePinned(it) },
                    onDelete = { noteViewModel.deleteNote(navController) },
                )
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                EditableTitle(
                    value = noteViewModel.uiState.title,
                    onChange = { noteViewModel.updateTitle(it) },
                    modifier = Modifier.fillMaxWidth(),
                )
                TextField(
                    value = noteViewModel.uiState.content,
                    onValueChange = { noteViewModel.updateContent(it) },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }
        }
    }
    LaunchedEffect(note) {
        if (noteViewModel.uiState.isEmpty()) {
            noteViewModel.loadNoteData()
        }
    }
}

