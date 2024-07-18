package com.pproject.sharednotes.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pproject.sharednotes.presentation.navigation.AppNavigation
import com.pproject.sharednotes.ui.theme.SharedNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SharedNotesTheme {
                AppNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SharedNotesTheme {
        AppNavigation()
    }
}
