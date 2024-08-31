package com.pproject.sharednotes.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pproject.sharednotes.data.local.entity.Note
import com.pproject.sharednotes.presentation.navigation.AppScreens

@Composable
fun NoteCard(
    note: Note,
    activeUser: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
    Surface(
        onClick = {
            navController.navigate("${AppScreens.NoteScreen.route}/${activeUser}/${note.noteId}")
        },
        shape = RoundedCornerShape(15.dp),
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 50.dp),
            modifier = modifier
                .height(200.dp)
                .width(150.dp),
            colors = CardDefaults.cardColors(
                containerColor = color
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    if (note.pinned) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = null,
                            modifier = Modifier
                                .size(15.dp)
                                .rotate(-45f)
                        )
                    }
                    if (note.title != "") {
                        Text(
                            text = note.title,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(0.4f, false)
                                .fillMaxWidth()
                        )
                    }
                }
                if (note.title != "") {
                    HorizontalDivider(color = MaterialTheme.colorScheme.inverseOnSurface)
                }
                Text(
                    text = note.content,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(0.6f, false)
                        .fillMaxWidth()
                )
            }
        }
    }
}