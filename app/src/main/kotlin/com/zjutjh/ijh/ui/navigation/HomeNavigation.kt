package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.HomeScreen

const val HomeRoute = "home"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeScreen(
    onNavigateToLogin: () -> Unit
) {
    composable(HomeRoute) {
        HomeScreen(onNavigateToLogin = onNavigateToLogin)
    }
}

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) =
    this.navigate(HomeRoute, navOptions)

/**
 * Pop up previous existed screens and navigate to [HomeScreen] with new ViewModel
 */
fun NavController.popUpAndNavigateToHomeScreen() =
    this.navigate(HomeRoute) {
        popUpTo(HomeRoute) { inclusive = true }
    }


fun NavController.getHomeBackStackEntry(): NavBackStackEntry =
    this.getBackStackEntry(HomeRoute)


fun NavController.popUpToHomeScreen(): Boolean =
    this.popBackStack(HomeRoute, inclusive = false)

