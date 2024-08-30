package com.pproject.sharednotes.presentation.screens.home.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pproject.sharednotes.R
import com.pproject.sharednotes.data.db.entity.Folder
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.presentation.common.BasicIconButton
import com.pproject.sharednotes.presentation.common.NoteCard
import com.pproject.sharednotes.presentation.navigation.AppScreens

@Composable
fun FolderHorizontalGrid(
    folder: Folder?,
    notes: List<Note>,
    activeUser: String,
    navController: NavController,
    onCreateNote: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteFolder: ((Folder) -> Unit)? = null,
) {
    var onClick = {}
    var title = stringResource(R.string.without_folder)
    var icon = Icons.Outlined.Folder
    var onCreateNoteFolder: Int? = null

    if (folder != null) {
        onClick = {
            navController.navigate(
                "${AppScreens.FolderScreen.route}/${activeUser}/false/${folder.folderId}"
            )
        }
        title = folder.title
        icon = Icons.Default.Folder
        onCreateNoteFolder = folder.folderId
    }
    Surface(
        color = Color.Transparent,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                    Text(
                        text = title,
                        fontSize = 20.sp,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicIconButton(
                        icon = Icons.Default.PostAdd,
                        onClick = { onCreateNote(onCreateNoteFolder) },
                    )
                    if (folder != null && onDeleteFolder != null) {
                        BasicIconButton(
                            icon = Icons.Default.Delete,
                            onClick = { onDeleteFolder(folder) }
                        )
                    }
                }
            }
            Divider()
            LazyRow(
                modifier = Modifier.height(200.dp),
            ) {
                items(
                    items = notes,
                ) {
                    NoteCard(
                        note = it,
                        activeUser = activeUser,
                        navController = navController,
                        modifier = Modifier.padding(2.dp),
                    )
                }
            }
        }
    }
}