package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.LoginScreen

private const val LoginRoute = "login"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginScreen(
    onPopBackStack: () -> Unit,
    onPopUpAndNavigateToHomeScreen: () -> Unit,
) {
    composable(LoginRoute) {
        LoginScreen(
            onNavigateBack = onPopBackStack,
            onNavigateContinue = onPopUpAndNavigateToHomeScreen
        )
    }
}

fun NavController.navigateToLoginScreen(navOptions: NavOptions?) =
    this.navigate(LoginRoute, navOptions)
