package com.pproject.sharednotes.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun Header(
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    buttons: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier,
        ) {
            BasicIconButton(
                icon = Icons.Default.ArrowBackIosNew,
                onClick = onClickBack
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier,
        ) {
            buttons()
        }
    }
}