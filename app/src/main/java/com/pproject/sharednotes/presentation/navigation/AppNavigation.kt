package com.pproject.sharednotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pproject.sharednotes.presentation.screens.folder.FolderScreen
import com.pproject.sharednotes.presentation.screens.home.HomeScreen
import com.pproject.sharednotes.presentation.screens.login.LoginScreen
import com.pproject.sharednotes.presentation.screens.note.NoteScreen
import com.pproject.sharednotes.presentation.screens.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(
            route = "${AppScreens.NoteScreen.route}/{user}/{${AppScreens.NoteScreen.argument}}",
            arguments = listOf(
                navArgument("user") {
                    type = NavType.StringType
                },
                navArgument(AppScreens.NoteScreen.argument) {
                    type = NavType.IntType
                },
            ),
        ) {
            NoteScreen(navController)
        }
        composable(
            route = "${AppScreens.FolderScreen.route}/{user}/{newFolder}/{${AppScreens.FolderScreen.argument}}",
            arguments = listOf(
                navArgument("user") {
                    type = NavType.StringType
                },
                navArgument("newFolder") {
                    type = NavType.BoolType
                },
                navArgument(AppScreens.FolderScreen.argument) {
                    type = NavType.IntType
                },
            ),
        ) {
            FolderScreen(navController)
        }
        composable(
            route = "${AppScreens.HomeScreen.route}/{user}",
            arguments = listOf(
                navArgument("user") {
                    type = NavType.StringType
                },
            ),
        ) {
            HomeScreen(navController)
        }
    }
}