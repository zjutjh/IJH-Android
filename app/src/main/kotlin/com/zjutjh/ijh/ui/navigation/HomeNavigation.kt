package com.zjutjh.ijh.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.zjutjh.ijh.ui.screen.HomeRoute

const val homeRoute = "home"

fun NavGraphBuilder.homeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    composable(homeRoute) {
        HomeRoute(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToCalendar = onNavigateToCalendar,
            onNavigateToAbout = onNavigateToAbout,
        )
    }
}

fun NavController.navigateToHome(navOptions: NavOptions? = null) =
    this.navigate(homeRoute, navOptions)

/**
 * Pop up previous existed screens and navigate to [HomeRoute] with new ViewModel
 */
fun NavController.popUpAndNavigateToHome() =
    this.navigate(homeRoute) {
        popUpTo(homeRoute) { inclusive = true }
    }

fun NavController.popUpToHome(): Boolean =
    this.popBackStack(homeRoute, inclusive = false)

