package com.pproject.sharednotes.presentation.screens.note.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pproject.sharednotes.R
import com.pproject.sharednotes.data.entity.Note
import com.pproject.sharednotes.presentation.common.BasicIconButton
import com.pproject.sharednotes.presentation.common.BasicIconToggleButton
import com.pproject.sharednotes.presentation.common.Header
import com.pproject.sharednotes.presentation.common.dialog.ConfirmationDialog
import com.pproject.sharednotes.presentation.common.dialog.NameListManagerDialog
import com.pproject.sharednotes.presentation.common.dialog.NameListSelectorDialog


@Composable
fun NoteHeader(
    onClickBack: () -> Unit,
    folderId: Int?,
    folderList: List<Pair<Int, String>>,
    onChangeFolder: (Int?) -> Unit,
    onAddCollaborator: (String) -> Unit,
    onDeleteCollaborator: (String) -> Unit,
    collaboratorList: List<String>,
    isPinned: Boolean,
    onChangePinned: (Boolean) -> Unit,
    situation: Note.Situation,
    onChangeArchive: (Boolean) -> Unit,
    onChangeDelete: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val openFolderDialog = remember { mutableStateOf(false) }
    val openConfirmDeleteDialog = remember { mutableStateOf(false) }
    val openCollaboratorsDialog = remember { mutableStateOf(false) }

    Header(
        onClickBack = onClickBack,
        modifier = modifier
    ) {
        BasicIconToggleButton(
            icon = Icons.Outlined.Folder,
            toggledIcon = Icons.Default.Folder,
            isToggled = folderId != null,
            onChange = { openFolderDialog.value = true },
        )
        BasicIconToggleButton(
            icon = Icons.Outlined.PersonAdd,
            toggledIcon = Icons.Default.PersonAdd,
            isToggled = collaboratorList.isNotEmpty(),
            onChange = { openCollaboratorsDialog.value = true },
        )
        BasicIconToggleButton(
            icon = Icons.Outlined.PushPin,
            toggledIcon = Icons.Default.PushPin,
            isToggled = isPinned,
            onChange = onChangePinned,
        )
        BasicIconToggleButton(
            icon = Icons.Outlined.Archive,
            toggledIcon = Icons.Default.Unarchive,
            isToggled = situation == Note.Situation.ARCHIVED,
            onChange = onChangeArchive,
        )
        BasicIconToggleButton(
            icon = Icons.Outlined.Delete,
            toggledIcon = Icons.Default.Delete,
            isToggled = situation == Note.Situation.DELETED,
            onChange = { openConfirmDeleteDialog.value = true },
        )
    }

    if (openFolderDialog.value) {
        NameListSelectorDialog(
            list = folderList,
            icon = Icons.Default.Folder,
            noneSelectionIcon = Icons.Outlined.Folder,
            selectedId = folderId,
            onCloseDialog = { openFolderDialog.value = false },
            onAcceptDialog = onChangeFolder,
        )
    }
    if (openConfirmDeleteDialog.value) {
        if (situation == Note.Situation.DELETED) {
            ConfirmationDialog(
                message = stringResource(R.string.do_you_want_to_restore_this_note),
                onClose = { openConfirmDeleteDialog.value = false },
                onConfirm = { onChangeDelete(false) }
            )
        } else {
            ConfirmationDialog(
                message = stringResource(R.string.do_you_want_to_delete_this_note),
                submessage = stringResource(R.string.it_can_only_be_read_and_will_be_deleted_permanently_on_a_month),
                onClose = { openConfirmDeleteDialog.value = false },
                onConfirm = { onChangeDelete(true) }
            )
        }
    }
    if (openCollaboratorsDialog.value) {
        NameListManagerDialog(
            list = stringToPair(collaboratorList),
            icon = Icons.Default.Person,
            onAddElement = onAddCollaborator,
            onDeleteElement = onDeleteCollaborator,
            onEditElement = null,
            onCloseDialog = { openCollaboratorsDialog.value = false }
        )
    }
}

fun stringToPair(list: List<String>): List<Pair<String, String>> {
    var result: List<Pair<String, String>> = emptyList()
    for (item in list) {
        result = result.plus(Pair(item, item))
    }
    return result
}