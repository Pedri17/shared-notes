package com.pproject.sharednotes.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BackNavigationHeader(
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    backNavigationIcon: ImageVector = Icons.Default.ArrowBackIosNew,
    centerComponent: (@Composable () -> Unit)? = null,
    rightComponent: (@Composable () -> Unit)? = null,
) {
    Header(
        modifier = modifier,
        leftComponent = {
            BasicIconButton(
                icon = backNavigationIcon,
                onClick = onClickBack
            )
        },
        centerComponent = centerComponent,
        rightComponent = rightComponent,
    )
}