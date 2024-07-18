package com.pproject.sharednotes.presentation.common


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

@Composable
fun DoubleSquareRoundedButton(
    textLeftButton: String,
    textRightButton: String,
    onClickLeftButton: () -> Unit,
    onClickRightButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Button(
            onClick = onClickLeftButton,
            shape = RoundedCornerShape(
                topStart = 5.dp,
                bottomStart = 5.dp,
                topEnd = 0.dp,
                bottomEnd = 0.dp,
            ),
        ) {
            Text(textLeftButton)
        }
        Button(
            onClick = onClickRightButton,
            shape = RoundedCornerShape(
                topStart = 0.dp,
                bottomStart = 0.dp,
                topEnd = 5.dp,
                bottomEnd = 5.dp,
            ),
        ) {
            Text(textRightButton)
        }
    }
}