package com.pproject.sharednotes.presentation.screens.folder.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.pproject.sharednotes.R
import com.pproject.sharednotes.presentation.common.BasicIconButton
import com.pproject.sharednotes.presentation.common.BasicIconToggleButton
import com.pproject.sharednotes.presentation.common.Header
import com.pproject.sharednotes.presentation.common.dialog.ConfirmationDialog

@Composable
fun FolderHeader(
    onClickBack: () -> Unit,
    onChangeCanEditTitle: (Boolean) -> Unit,
    canEditTitle: Boolean,
    onAddNewNote: () -> Unit,
    onDeleteFolder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openDeleteDialog = remember { mutableStateOf(false) }
    Header(
        onClickBack = onClickBack,
        modifier = modifier,
    ) {
        BasicIconToggleButton(
            icon = Icons.Outlined.Edit,
            toggledIcon = Icons.Default.Edit,
            isToggled = canEditTitle,
            onChange = onChangeCanEditTitle,
        )
        BasicIconButton(
            icon = Icons.Default.PostAdd,
            onClick = onAddNewNote
        )
        BasicIconButton(
            icon = Icons.Default.Delete,
            onClick = { openDeleteDialog.value = true }
        )
    }

    if (openDeleteDialog.value) {
        ConfirmationDialog(
            message = stringResource(R.string.do_you_want_to_delete_permanently_this_folder),
            submessage = stringResource(R.string.notes_will_not_be_deleted_but_will_remain_without_an_assigned_folder),
            onClose = { openDeleteDialog.value = false },
            onConfirm = onDeleteFolder
        )
    }
}

