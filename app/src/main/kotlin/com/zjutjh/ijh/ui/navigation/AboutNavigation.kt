package com.zjutjh.ijh.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.zjutjh.ijh.ui.screen.AboutRoute

private const val aboutRoute = "about"

fun NavGraphBuilder.aboutScreen(
    onPopBackStack: () -> Unit,
) {
    composable(aboutRoute) {
        AboutRoute(onNavigateBack = onPopBackStack)
    }
}

fun NavController.navigateToAbout(navOptions: NavOptions?) {
    this.navigate(aboutRoute, navOptions)
}