package com.pproject.sharednotes.presentation.common

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun Header(
    modifier: Modifier = Modifier,
    leftComponent: (@Composable () -> Unit)? = null,
    centerComponent: (@Composable () -> Unit)? = null,
    rightComponent: (@Composable () -> Unit)? = null,
) {
    var leftModifier: Modifier = Modifier
    var centerModifier: Modifier = Modifier
    var centerArrangement: Arrangement.Horizontal = Arrangement.Center

    if (rightComponent == null && centerComponent != null) {
        leftModifier = Modifier.fillMaxWidth(0.33f)
        centerModifier = Modifier.fillMaxWidth()
        centerArrangement = Arrangement.Start
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = leftModifier,
        ) {
            leftComponent?.let {
                leftComponent()
            }
        }
        Row(
            horizontalArrangement = centerArrangement,
            modifier = centerModifier,
        ) {
            centerComponent?.let {
                centerComponent()
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier,
        ) {
            rightComponent?.let {
                rightComponent()
            }
        }
    }
}


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