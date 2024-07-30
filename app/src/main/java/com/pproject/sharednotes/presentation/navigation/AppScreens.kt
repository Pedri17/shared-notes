package com.pproject.sharednotes.presentation.navigation

sealed class AppScreens(val route: String) {
    data object SplashScreen : AppScreens("splash_screen")
    data object LoginScreen : AppScreens("login_screen")
    data object RegisterScreen : AppScreens("register_screen")
    data object NoteScreen : AppScreens("note_screen")
    data object FolderScreen : AppScreens("folder_screen")
    data object HomeScreen : AppScreens("home_screen")
}