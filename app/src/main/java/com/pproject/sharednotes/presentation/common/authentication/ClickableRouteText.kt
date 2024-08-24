package com.pproject.sharednotes.presentation.common.authentication

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController

@Composable
fun ClickableRouteText(
    navController: NavController,
    text: String,
    route: String,
    color: Color = Color.Cyan
) {
    Text(
        text = text,
        modifier = Modifier.clickable {
            navController.navigate(route)
        },
        style = TextStyle(color)
    )
}