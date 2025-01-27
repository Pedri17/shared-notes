package com.pproject.sharednotes.presentation.navigation

sealed class AppScreens(
    val route: String,
    val argument: String = ""
) {
    data object LoginScreen : AppScreens("login_screen")
    data object RegisterScreen : AppScreens("register_screen")
    data object NoteScreen : AppScreens("note_screen", "noteId")
    data object FolderScreen : AppScreens("folder_screen", "folderId")
    data object HomeScreen : AppScreens("home_screen")
}