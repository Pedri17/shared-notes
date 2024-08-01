package com.pproject.sharednotes.presentation.screens.note.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.pproject.sharednotes.presentation.common.BasicIconToggleButton

@Composable
fun NoteBottomBar(
    color: Color?,
    onChangeColor: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openColorPopup = remember { mutableStateOf(false) }

    BottomAppBar {
        BasicIconToggleButton(
            icon = Icons.Outlined.Palette,
            toggledIcon = Icons.Default.Palette,
            isToggled = color != null,
            onChange = { openColorPopup.value = true },
            modifier = modifier.height(30.dp)
        )
    }

    if (openColorPopup.value) {
        Popup(
            alignment = Alignment.BottomEnd,
            properties = PopupProperties(
                dismissOnClickOutside = true
            )
        ) {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 30.dp
                ),
                shape = RoundedCornerShape(0.dp),
                modifier = modifier
                    .fillMaxHeight(0.3f)
                    .fillMaxWidth(),
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("Pruebaaaaaaaa")
                }
            }

        }
    }
}

