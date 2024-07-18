package com.pproject.sharednotes.presentation.screens.note.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.pproject.sharednotes.data.entity.Note
import com.pproject.sharednotes.presentation.common.ConfirmationDialog


@Composable
fun Header(
    onClickBack: () -> Unit,
    folderId: Int,
    folderList: List<Pair<Int, String>>,
    onChangeFolder: (Int) -> Unit,
    onClickShare: () -> Unit,
    isPinned: Boolean,
    onChangePinned: (Boolean) -> Unit,
    situation: Note.Situation,
    onChangeArchive: (Boolean) -> Unit,
    onChangeDelete: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val openFolderDialog = remember { mutableStateOf(false) }
    val openConfirmDeleteDialog = remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier,
        ) {
            NoteIconButton(
                icon = Icons.Default.ArrowBackIosNew,
                onClick = onClickBack
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            NoteIconToggleButton(
                icon = Icons.Outlined.Folder,
                toggledIcon = Icons.Default.Folder,
                isToggled = folderId > -1,
                onChange = { openFolderDialog.value = true },
            )
            NoteIconButton(
                icon = Icons.Default.Share,
                onClick = onClickShare,
            )
            NoteIconToggleButton(
                icon = Icons.Outlined.PushPin,
                toggledIcon = Icons.Default.PushPin,
                isToggled = isPinned,
                onChange = onChangePinned,
            )
            NoteIconToggleButton(
                icon = Icons.Outlined.Archive,
                toggledIcon = Icons.Default.Unarchive,
                isToggled = situation == Note.Situation.ARCHIVED,
                onChange = onChangeArchive,
            )
            NoteIconToggleButton(
                icon = Icons.Outlined.Delete,
                toggledIcon = Icons.Default.Delete,
                isToggled = situation == Note.Situation.DELETED,
                onChange = { openConfirmDeleteDialog.value = true },
            )
        }
    }

    if (openFolderDialog.value) {
        UpperTitleSelectorDialog(
            list = folderList,
            selectedId = folderId,
            onCloseDialog = { openFolderDialog.value = false },
            onAcceptDialog = onChangeFolder,
            modifier = Modifier.heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f)
        )
    }
    if (openConfirmDeleteDialog.value) {
        if (situation == Note.Situation.DELETED) {
            ConfirmationDialog(
                message = "Do you want to restore this note?",
                onClose = { openConfirmDeleteDialog.value = false },
                onConfirm = { onChangeDelete(false) }
            )
        } else {
            ConfirmationDialog(
                message = "Do you want to delete this note?",
                submessage = "It can only be read and will be deleted permanently on a month",
                onClose = { openConfirmDeleteDialog.value = false },
                onConfirm = { onChangeDelete(true) }
            )
        }


    }
}

@Composable
fun NoteIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun NoteIconToggleButton(
    icon: ImageVector,
    toggledIcon: ImageVector,
    isToggled: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconToggleButton(
        checked = isToggled,
        onCheckedChange = onChange,
        enabled = enabled,
        modifier = modifier,
    ) {
        if (isToggled) {
            Icon(
                imageVector = toggledIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}