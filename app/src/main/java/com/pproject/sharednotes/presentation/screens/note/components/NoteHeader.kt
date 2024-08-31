package com.pproject.sharednotes.presentation.screens.note.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pproject.sharednotes.R
import com.pproject.sharednotes.presentation.common.header.BackNavigationHeader
import com.pproject.sharednotes.presentation.common.button.BasicIconButton
import com.pproject.sharednotes.presentation.common.button.BasicIconToggleButton
import com.pproject.sharednotes.presentation.common.dialog.ConfirmationDialog
import com.pproject.sharednotes.presentation.common.dialog.NameListManagerDialog
import com.pproject.sharednotes.presentation.common.dialog.NameListSelectorDialog

@Composable
fun NoteHeader(
    users: List<String>,
    isPinnedNote: Boolean,
    selectedFolder: Int?,
    folderList: List<Pair<Int, String>>,
    onClickBack: () -> Unit,
    onChangeFolder: (Int?, Int?) -> Unit,
    onAddCollaborator: (String) -> Unit,
    onDeleteCollaborator: (String) -> Unit,
    onChangePinned: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openFolderDialog = remember { mutableStateOf(false) }
    val openConfirmDeleteDialog = remember { mutableStateOf(false) }
    val openCollaboratorsDialog = remember { mutableStateOf(false) }

    BackNavigationHeader(
        onClickBack = onClickBack,
        modifier = modifier
    ) {
        BasicIconToggleButton(
            icon = Icons.Outlined.Folder,
            toggledIcon = Icons.Default.Folder,
            isToggled = selectedFolder != null,
            onChange = { openFolderDialog.value = true },
        )
        BasicIconToggleButton(
            icon = Icons.Outlined.PersonAdd,
            toggledIcon = Icons.Default.PersonAdd,
            isToggled = users.isNotEmpty(),
            onChange = { openCollaboratorsDialog.value = true },
        )
        BasicIconToggleButton(
            icon = Icons.Outlined.PushPin,
            toggledIcon = Icons.Default.PushPin,
            isToggled = isPinnedNote,
            onChange = onChangePinned,
        )
        BasicIconButton(
            icon = Icons.Default.Delete,
            onClick = { openConfirmDeleteDialog.value = true },
        )
    }

    if (openFolderDialog.value) {
        NameListSelectorDialog(
            list = folderList,
            icon = Icons.Default.Folder,
            headerTitle = stringResource(R.string.folder),
            noneSelectionIcon = Icons.Outlined.Folder,
            selectedId = selectedFolder,
            onCloseDialog = { openFolderDialog.value = false },
            onSaveDialog = { onChangeFolder(selectedFolder, it) },
        )
    }
    if (openConfirmDeleteDialog.value) {
        ConfirmationDialog(
            message = stringResource(R.string.do_you_want_to_delete_this_note),
            submessage = stringResource(R.string.it_will_be_permanently_deleted_for_you_but_collaborators_will_be_able_to_continue_editing_it),
            onClose = { openConfirmDeleteDialog.value = false },
            onConfirm = { onDelete() }
        )
    }
    if (openCollaboratorsDialog.value) {
        NameListManagerDialog(
            list = stringToPair(users),
            headerTitle = stringResource(R.string.collaborators),
            icon = Icons.Default.Person,
            onAddElement = onAddCollaborator,
            onDeleteElement = onDeleteCollaborator,
            onCloseDialog = { openCollaboratorsDialog.value = false },
            elementModifier = Modifier.height(56.dp)
        )
    }
}

fun stringToPair(list: List<String>): List<Pair<String, String>> {
    val result: MutableList<Pair<String, String>> = mutableListOf()
    for (item in list) {
        result.add(Pair(item, item))
    }
    return result.toList()
}