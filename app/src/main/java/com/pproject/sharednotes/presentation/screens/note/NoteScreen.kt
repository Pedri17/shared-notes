package com.pproject.sharednotes.presentation.screens.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.data.entity.Note
import com.pproject.sharednotes.presentation.screens.note.components.EditableTitle
import com.pproject.sharednotes.presentation.screens.note.components.Header
import com.pproject.sharednotes.presentation.screens.note.components.Section
import com.pproject.sharednotes.presentation.screens.note.components.SectionsField

var listTest: List<Pair<Int, String>> = listOf(
    Pair(0, "Nombre Cero"),
    Pair(1, "Uno"),
    Pair(2, "Dos"),
    Pair(3, "Nombre Tres largo para ver cómo se comporta"),
    Pair(4, "Cuatro"),
    Pair(5, "otro más a ver que pasa si es bastante largo, habrá que probar cositas estoy cansado jefe"),
    Pair(6, "Seis"),
    Pair(7, "Siete"),
    Pair(8, "Ocho"),
    Pair(9, "Nueve"),
    Pair(10, "Diez"),
    Pair(11, "Once"),
    Pair(12, "El ultimo ya estoy cansado jefe a ver que pasa si pongo nombres muy largos supongo que tendré que reducirlos pero de momento tirando")
)

@Composable
fun NoteScreen(
    navController: NavController,
    noteViewModel: NoteViewModel = viewModel()
) {
    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Header(
                onClickBack = { navController.popBackStack() },
                folderId = noteViewModel.folderId,
                onChangeFolder = { noteViewModel.updateFolder(it) },
                folderList = listTest,
                onClickShare = {},
                isPinned = noteViewModel.pinned,
                onChangePinned = { noteViewModel.updatePinned(it) },
                situation = noteViewModel.situation,
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
            EditableTitle(
                value = noteViewModel.title,
                onChange = {
                    noteViewModel.updateTitle(it)
                },
                modifier = Modifier.fillMaxWidth(),
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

