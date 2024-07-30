package com.pproject.sharednotes.presentation.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pproject.sharednotes.R

@Composable
fun <T : Any> NameListSelectorDialog(
    list: List<Pair<T, String>>,
    icon: ImageVector,
    selectedId: T?,
    onCloseDialog: () -> Unit,
    onAcceptDialog: (T?) -> Unit,
    modifier: Modifier = Modifier,
    noneSelectionIcon: ImageVector? = null,
) {
    var selectedTitleId by remember { mutableStateOf(selectedId) }
    Dialog(
        onDismissRequest = onCloseDialog
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 30.dp
            ),
            shape = RoundedCornerShape(1.dp),
            modifier = modifier.heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f),
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
                        Text(stringResource(R.string.accept))
                    }
                    TextButton(
                        onClick = onCloseDialog
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        }
    }
}