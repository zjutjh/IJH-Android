package com.zjutjh.ijh.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun IJHApp() {
    IJHAppNavHost()
}


@OptIn(ExperimentalMaterial3Api::class)
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
            HomeScreen()
        }
    }
}
