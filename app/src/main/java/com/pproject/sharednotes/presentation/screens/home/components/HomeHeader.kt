package com.pproject.sharednotes.presentation.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Output
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pproject.sharednotes.R
import com.pproject.sharednotes.presentation.common.button.BasicIconButton
import com.pproject.sharednotes.presentation.common.button.BasicIconToggleButton
import com.pproject.sharednotes.presentation.common.header.Header
import com.pproject.sharednotes.presentation.common.dialog.NameListManagerDialog

@Composable
fun HomeHeader(
    notificationPairs: List<Pair<Int, String>>,
    onAcceptNotification: (Int) -> Unit,
    onDeleteNotification: (Int) -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openNotificationDialog = remember { mutableStateOf(false) }
    Column {
        Header(
            modifier = modifier,
            leftComponent = {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
            },
            rightComponent = {
                Row {
                    BasicIconToggleButton(
                        icon = Icons.Outlined.Notifications,
                        toggledIcon = Icons.Default.Notifications,
                        isToggled = notificationPairs.isNotEmpty(),
                        onChange = { openNotificationDialog.value = !openNotificationDialog.value },
                    )
                    BasicIconButton(
                        icon = Icons.Outlined.Output,
                        onClick = onLogOut,
                    )
                }

            },
        )
        Divider(
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
    }
    if (openNotificationDialog.value) {
        NameListManagerDialog(
            list = notificationPairs,
            headerTitle = stringResource(R.string.notifications),
            icon = Icons.Default.Mail,
            onCloseDialog = { openNotificationDialog.value = false },
            onDeleteElement = onDeleteNotification,
            onAcceptElement = onAcceptNotification,
        )
    }
}