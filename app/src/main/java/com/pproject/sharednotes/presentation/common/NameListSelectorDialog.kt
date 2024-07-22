package com.pproject.sharednotes.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.outlined.Folder
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pproject.sharednotes.presentation.common.SelectableTitleElement

@Composable
fun NameListSelectorDialog(
    list: List<Pair<Int, String>>,
    icon: ImageVector,
    selectedId: Int,
    onCloseDialog: () -> Unit,
    onAcceptDialog: (Int) -> Unit,
    modifier: Modifier = Modifier,
    noneSelectionIcon: ImageVector? = null,
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
                    if (noneSelectionIcon != null){
                        item {
                            SelectableTitleElement(
                                text = "None",
                                noneSelectionIcon,
                                onClick = { selectedTitleId = -1 },
                                emptyIcon = noneSelectionIcon,
                                selectedOption = selectedTitleId < 0,
                            )
                        }
                    }
                    items(
                        items = pairList,
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