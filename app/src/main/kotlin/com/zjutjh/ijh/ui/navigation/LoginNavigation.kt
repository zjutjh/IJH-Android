package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.LoginScreen

private const val loginRoute = "login"

@OptIn(ExperimentalAnimationApi::class)
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
