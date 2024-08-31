package com.pproject.sharednotes.presentation.common.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BasicIconToggleButton(
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