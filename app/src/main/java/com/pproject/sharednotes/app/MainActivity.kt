package com.pproject.sharednotes.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pproject.sharednotes.presentation.navigation.AppNavigation
import com.pproject.sharednotes.presentation.theme.SharedNotesTheme

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