package com.pproject.sharednotes.presentation.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pproject.sharednotes.data.entity.Folder
import com.pproject.sharednotes.data.entity.Note
import com.pproject.sharednotes.presentation.common.NoteCard
import com.pproject.sharednotes.presentation.navigation.AppScreens

@Composable
fun FolderHorizontalGrid(
    folder: Folder,
    navController: NavController,
    onGetNote: (Int) -> Note,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = Color.Transparent,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        onClick = {
            navController.navigate(
                AppScreens.FolderScreen.route + "/" + folder.id
            )
        }
    ) {
        Column {
            Text(
                text = folder.title.text,
                fontSize = 20.sp,
            )
            Divider()
            LazyRow(
                modifier = Modifier.height(200.dp),
            ) {
                items(
                    items = folder.getOrderedNotes()
                ) {
                    NoteCard(
                        note = onGetNote(it),
                        navController = navController,
                        modifier = Modifier.padding(2.dp),
                    )
                }
            }
        }
    }
}