package com.zjutjh.ijh.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.zjutjh.ijh.ui.screen.LoginScreen

private const val loginRoute = "login"

fun NavGraphBuilder.loginScreen(
    onPopBackStack: () -> Unit,
    onPopUpAndNavigateToHomeScreen: () -> Unit,
) {
    composable(loginRoute) {
        LoginScreen(
            onNavigateBack = onPopBackStack,
            onNavigateContinue = onPopUpAndNavigateToHomeScreen
        )
    }
}

fun NavController.navigateToLogin(navOptions: NavOptions?) =
    this.navigate(loginRoute, navOptions)
