package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.HomeRoute

const val homeRoute = "home"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToClassSchedule: () -> Unit,
) {
    composable(homeRoute) {
        HomeRoute(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToClassSchedule = onNavigateToClassSchedule,
        )
    }
}

fun NavController.navigateToHome(navOptions: NavOptions? = null) =
    this.navigate(homeRoute, navOptions)

/**
 * Pop up previous existed screens and navigate to [HomeScreen] with new ViewModel
 */
fun NavController.popUpAndNavigateToHome() =
    this.navigate(homeRoute) {
        popUpTo(homeRoute) { inclusive = true }
    }

fun NavController.popUpToHome(): Boolean =
    this.popBackStack(homeRoute, inclusive = false)

