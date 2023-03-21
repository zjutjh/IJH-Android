package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.AboutRoute

private const val aboutRoute = "about"

@OptIn(ExperimentalAnimationApi::class)
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