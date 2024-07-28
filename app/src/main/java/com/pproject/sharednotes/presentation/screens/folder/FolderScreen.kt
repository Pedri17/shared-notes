package com.pproject.sharednotes.presentation.screens.folder

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.presentation.common.BasicIconButton
import com.pproject.sharednotes.presentation.common.Header
import com.pproject.sharednotes.presentation.common.NoteCard
import com.pproject.sharednotes.presentation.common.dialog.TitleElement
import com.pproject.sharednotes.presentation.navigation.AppScreens
import com.pproject.sharednotes.presentation.screens.folder.components.FolderHeader
import com.pproject.sharednotes.presentation.screens.folder.components.OnTriggerEditableTitle

@Composable
fun FolderScreen(
    navController: NavController,
    viewModel: FolderViewModel = viewModel(),
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
                    viewModel.updateCanEditTitle(it)
                    viewModel.setTitleCursorToEnd()
                },
                canEditTitle = viewModel.titleState.canEdit,
                onAddNewNote = {
                    //TODO: creación de nueva nota vacía (con esta carpeta preseleccionada) y se abra la pantalla con dicha nota
                    navController.navigate(
                        AppScreens.NoteScreen.route + "/"
                                + viewModel.createNoteInThisFolder()
                    )
                },
                onDeleteFolder = {
                    // TODO: añadir eliminación definitiva de la carpeta
                    navController.popBackStack()
                },
            )
            OnTriggerEditableTitle(
                text = viewModel.titleState.title,
                onChange = { viewModel.updateTitle(it) },
                onDone = { viewModel.updateCanEditTitle(false) },
                enabled = viewModel.titleState.canEdit,
                focusRequester = focusRequester,
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(
                    items = viewModel.getOrderedNotes(),
                    key = { it }
                ) { noteID ->
                    NoteCard(
                        note = viewModel.getNoteFromId(noteID),
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}