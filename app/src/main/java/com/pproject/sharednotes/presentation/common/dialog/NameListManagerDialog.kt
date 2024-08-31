package com.pproject.sharednotes.presentation.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.pproject.sharednotes.presentation.common.dialog.element.ManageableTitleElement
import com.pproject.sharednotes.presentation.common.dialog.element.ToAddTitleElement
import kotlinx.coroutines.launch

data class ToAddElementState(
    var name: String = "",
    var isActive: Boolean = false,
)

@Composable
fun <T : Any> NameListManagerDialog(
    list: List<Pair<T, String>>,
    headerTitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    elementModifier: Modifier = Modifier,
    onAddElement: ((String) -> Unit)? = null,
    onDeleteElement: ((T) -> Unit)? = null,
    onAcceptElement: ((T) -> Unit)? = null,
    onCloseDialog: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val columnState = rememberLazyListState()

    var toAddElementState by remember { mutableStateOf(ToAddElementState()) }

    FullScreenDialog<Unit>(
        onCloseDialog = onCloseDialog,
        headerTitle = headerTitle,
        modifier = modifier,
    ) {
        LazyColumn(
            state = columnState,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            items(
                items = list,
                key = { it.first }
            ) { element ->
                ManageableTitleElement(
                    text = element.second,
                    icon = icon,
                    onAccept = if (onAcceptElement != null) {
                        { onAcceptElement(element.first) }
                    } else {
                        null
                    },
                    onDelete = if (onDeleteElement != null) {
                        { onDeleteElement(element.first) }
                    } else {
                        null
                    },
                    modifier = elementModifier
                )
                HorizontalDivider(modifier = Modifier.padding(5.dp))
            }
            if (onAddElement != null && toAddElementState.isActive) {
                item {
                    ToAddTitleElement(
                        toEditText = toAddElementState.name,
                        onChange = {
                            toAddElementState = toAddElementState.copy(name = it)
                        },
                        icon = Icons.Default.Person,
                        modifier = Modifier.focusRequester(focusRequester),
                        onDone = {
                            onAddElement(toAddElementState.name)
                            toAddElementState = ToAddElementState()
                            coroutineScope.launch {
                                columnState.animateScrollToItem(list.size)
                            }
                        },
                    )
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                }
            }
        }
        if (onAddElement != null && !toAddElementState.isActive) {
            LargeFloatingActionButton(
                shape = FloatingActionButtonDefaults.largeShape,
                onClick = {
                    toAddElementState = toAddElementState.copy(isActive = true)
                    coroutineScope.launch {
                        columnState.animateScrollToItem(list.size)
                    }
                },
                modifier = Modifier
                    .size(50.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize)
                )
            }
        }
    }
}