package com.pproject.sharednotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pproject.sharednotes.presentation.screens.folder.FolderScreen
import com.pproject.sharednotes.presentation.screens.login.LoginScreen
import com.pproject.sharednotes.presentation.screens.note.NoteScreen
import com.pproject.sharednotes.presentation.screens.register.RegisterScreen
import com.pproject.sharednotes.presentation.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {
        composable(route = AppScreens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(
            route = AppScreens.NoteScreen.route + "/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.IntType }),
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("noteID")?.let { NoteScreen(navController, it) }
        }
        composable(route = AppScreens.FolderScreen.route) {
            FolderScreen(navController)
        }
    }
}