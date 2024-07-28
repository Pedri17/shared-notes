package com.pproject.sharednotes.presentation.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.pproject.sharednotes.presentation.common.BasicIconButton


@Composable
fun TitleElement(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .height(56.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = fontWeight,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

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

@Composable
fun ManageableTitleElement(
    text: String,
    icon: ImageVector,
    onEdit: (() -> Unit)?,
    onDelete: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
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
            if (onEdit != null){
                BasicIconButton(
                    icon = Icons.Default.Edit,
                    onClick = onEdit
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

@Composable
fun ToAddTitleElement(
    toEditText: String,
    onChange: (String) -> Unit,
    icon: ImageVector,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.weight(0.8f),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
            TextField(
                value = toEditText,
                onValueChange = onChange,
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onDone() }),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.2f),
        ) {
            BasicIconButton(
                icon = Icons.Default.Check,
                onClick = onDone
            )
        }
    }
}