package com.pproject.sharednotes.presentation.common.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pproject.sharednotes.R

@Composable
fun ConfirmationDialog(
    message: String,
    onClose: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    submessage: String? = null,
) {
    Dialog(
        onDismissRequest = onClose,
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 30.dp
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = modifier,
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        text = message,
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp,
                        lineHeight = 21.sp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    )
                    if (submessage != null) {
                        Text(
                            text = submessage,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onClose
                    ) {
                        Text(stringResource(R.string.no))
                    }
                    TextButton(
                        onClick = {
                            onConfirm()
                            onClose()
                        }
                    ) {
                        Text(stringResource(R.string.yes))
                    }
                }
            }
        }
    }
}