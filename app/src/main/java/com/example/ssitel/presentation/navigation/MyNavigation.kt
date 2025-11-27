package com.example.ssitel.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ssitel.presentation.components.MyScaffold
import com.example.ssitel.presentation.screens.Home
import com.example.ssitel.presentation.screens.LoginScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument


@Composable
fun MyNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    MyScaffold {
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") { LoginScreen(navController) }

            composable(
                route = "home/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("id") ?: 0
                Home(navController = navController, userId = userId)
            }
        }
    }
}

