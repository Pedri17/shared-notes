package com.pproject.sharednotes.presentation.screens.note.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun UpperTitleSelectorDialog(
    list: List<Pair<Int, String>>,
    selectedId: Int,
    onCloseDialog: () -> Unit,
    onAcceptDialog: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pairList = remember { list.toMutableStateList() }
    var selectedTitleId by remember { mutableIntStateOf(selectedId) }
    Dialog(
        onDismissRequest = onCloseDialog
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 30.dp
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = modifier,
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .weight(0.9f)
                ) {
                    item {
                        SelectableTitle(
                            text = "None",
                            onClick = { selectedTitleId = -1 },
                            isEmptyOption = true,
                            selectedOption = selectedTitleId < 0,
                        )
                    }
                    items(
                        items = pairList,
                        key = { it.first }
                    ) { element ->
                        SelectableTitle(
                            text = element.second,
                            onClick = { selectedTitleId = element.first },
                            selectedOption = selectedTitleId == element.first,
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .weight(0.1f)
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 0.dp, start = 10.dp, end = 10.dp)
                ) {
                    TextButton(
                        onClick = {
                            onAcceptDialog(selectedTitleId)
                            onCloseDialog()
                        },
                    ) {
                        Text("Accept")
                    }
                    TextButton(
                        onClick = onCloseDialog
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun SelectableTitle(
    text: String,
    onClick: () -> Unit,
    selectedOption: Boolean,
    modifier: Modifier = Modifier,
    isEmptyOption: Boolean = false,
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
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.75f)
        ) {
            val folderIcon = if (isEmptyOption) Icons.Outlined.Folder else Icons.Default.Folder
            val weight = if (isEmptyOption) FontWeight.Bold else FontWeight.Normal
            Icon(
                imageVector = folderIcon,
                contentDescription = null
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = weight,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
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