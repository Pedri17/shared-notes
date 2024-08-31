package com.pproject.sharednotes.presentation.common.dialog.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SelectableTitleElement(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    selectedOption: Boolean,
    modifier: Modifier = Modifier,
    emptyIcon: ImageVector? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selectedOption,
                onClick = onClick,
                role = Role.RadioButton,
            )
    ) {
        val toUseIcon = emptyIcon ?: icon
        val weight = if (emptyIcon != null) FontWeight.Bold else FontWeight.Normal
        TitleElement(
            text = text,
            icon = toUseIcon,
            modifier = Modifier.weight(0.75f),
            fontWeight = weight,
        )
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.25f)
        ) {
            RadioButton(
                selected = selectedOption,
                onClick = null,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}