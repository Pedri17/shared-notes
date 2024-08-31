package com.pproject.sharednotes.presentation.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pproject.sharednotes.R
import com.pproject.sharednotes.presentation.common.dialog.element.SelectableTitleElement

@Composable
fun <T : Any> NameListSelectorDialog(
    list: List<Pair<T, String>>,
    icon: ImageVector,
    headerTitle: String,
    selectedId: T?,
    onCloseDialog: () -> Unit,
    onSaveDialog: (T?) -> Unit,
    modifier: Modifier = Modifier,
    noneSelectionIcon: ImageVector? = null,
) {
    var selectedTitleId by remember { mutableStateOf(selectedId) }
    FullScreenDialog(
        onCloseDialog = onCloseDialog,
        headerTitle = headerTitle,
        saveButton = FullScreenDialogSaveButton(selectedTitleId, onSaveDialog),
        modifier = modifier,
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            if (noneSelectionIcon != null) {
                item {
                    SelectableTitleElement(
                        text = stringResource(R.string.none),
                        noneSelectionIcon,
                        onClick = { selectedTitleId = null },
                        emptyIcon = noneSelectionIcon,
                        selectedOption = selectedTitleId == null,
                    )
                }
            }
            items(
                items = list,
                key = { it.first }
            ) { element ->
                SelectableTitleElement(
                    text = element.second,
                    icon,
                    onClick = { selectedTitleId = element.first },
                    selectedOption = selectedTitleId == element.first,
                )
            }
        }
    }
}