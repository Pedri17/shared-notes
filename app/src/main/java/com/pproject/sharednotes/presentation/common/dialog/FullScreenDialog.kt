package com.pproject.sharednotes.presentation.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pproject.sharednotes.R
import com.pproject.sharednotes.presentation.common.BackNavigationHeader

data class FullScreenDialogSaveButton<T : Any>(
    val toSaveElement: T?,
    val onSave: (T?) -> Unit,
)

@Composable
fun <T : Any> FullScreenDialog(
    onCloseDialog: () -> Unit,
    headerTitle: String,
    modifier: Modifier = Modifier,
    saveButton: FullScreenDialogSaveButton<T>? = null,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCloseDialog,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BackNavigationHeader(
                    onClickBack = onCloseDialog,
                    backNavigationIcon = Icons.Default.Close,
                    centerComponent = {
                        Text(
                            text = headerTitle,
                            fontSize = 22.sp,
                        )
                    },
                    rightComponent = if (saveButton != null) {
                        {
                            TextButton(
                                onClick = {
                                    saveButton.onSave(saveButton.toSaveElement)
                                    onCloseDialog()
                                },
                                modifier = Modifier.padding(end = 10.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.save),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else null,
                )
                content()
            }
        }
    }
}