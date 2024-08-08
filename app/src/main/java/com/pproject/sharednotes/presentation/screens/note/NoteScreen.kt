package com.pproject.sharednotes.presentation.screens.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.data.db.entity.Note
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
    val note by noteViewModel.note.observeAsState(Note())
    val folderPairNames by noteViewModel.allPairNameFolders.observeAsState(emptyList())
    Surface {
        Scaffold(
            topBar = {
                NoteHeader(
                    onClickBack = { navController.popBackStack() },
                    note = note,
                    users = users,
                    onChangeFolder = { noteViewModel.updateFolder(it) },
                    folderList = folderPairNames,
                    onAddCollaborator = { noteViewModel.insertCollaborator(it) },
                    onDeleteCollaborator = { noteViewModel.deleteCollaborator(it) },
                    onChangePinned = { noteViewModel.updatePinned(it) },
                    onChangeArchive = { isArchived: Boolean ->
                        if (!isArchived) {
                            noteViewModel.updateSituation(Note.Situation.ON_USE)
                        } else {
                            noteViewModel.updateSituation(Note.Situation.ARCHIVED)
                        }
                    },
                    onChangeDelete = { isDeleted: Boolean ->
                        if (!isDeleted) {
                            noteViewModel.updateSituation(Note.Situation.ON_USE)
                        } else {
                            noteViewModel.updateSituation(Note.Situation.DELETED)
                        }
                    }
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
                    value = note.title,
                    onChange = { noteViewModel.updateTitle(it) },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                SectionsField(
                    sections = noteViewModel.content,
                    onChange = {
                            section: Section,
                            newText: String
                        ->
                        noteViewModel.updateContent(section, newText)
                    },
                )
            }
        }
    }
}

