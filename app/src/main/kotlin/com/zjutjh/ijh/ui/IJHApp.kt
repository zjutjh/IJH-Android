package com.zjutjh.ijh.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zjutjh.ijh.ui.screen.HomeScreen
import com.zjutjh.ijh.ui.screen.LoginScreen

@Composable
fun IJHApp() {
    IJHAppNavHost()
}

@Composable
fun IJHAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = IJHRoute.HOME
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(IJHRoute.HOME) {
            HomeScreen {
                navController.navigate(IJHRoute.LOGIN) {
                    launchSingleTop = true
                }
            }
        }
        composable(IJHRoute.LOGIN) {
            LoginScreen()
        }
    }
}
