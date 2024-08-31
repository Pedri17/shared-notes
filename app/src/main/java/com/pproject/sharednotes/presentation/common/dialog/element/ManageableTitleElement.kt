package com.pproject.sharednotes.presentation.common.dialog.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.pproject.sharednotes.presentation.common.button.BasicIconButton

@Composable
fun ManageableTitleElement(
    text: String,
    icon: ImageVector,
    onAccept: (() -> Unit)?,
    onDelete: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        TitleElement(
            text = text,
            icon = icon,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onAccept != null) {
                BasicIconButton(
                    icon = Icons.Default.Check,
                    onClick = onAccept
                )
            }
            if (onDelete != null) {
                BasicIconButton(
                    icon = Icons.Default.Close,
                    onClick = onDelete
                )
            }
        }
    }
}