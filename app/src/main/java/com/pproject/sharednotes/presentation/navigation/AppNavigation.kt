package com.pproject.sharednotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pproject.sharednotes.presentation.screens.login.LoginScreen
import com.pproject.sharednotes.presentation.screens.note.NoteScreen
import com.pproject.sharednotes.presentation.screens.register.RegisterScreen
import com.pproject.sharednotes.presentation.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {
        composable(route = AppScreens.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(route = AppScreens.LoginScreen.route){
            LoginScreen(navController)
        }
        composable(route = AppScreens.RegisterScreen.route){
            RegisterScreen(navController)
        }
        composable(route = AppScreens.NoteScreen.route){
            NoteScreen(navController)
        }
    }
}