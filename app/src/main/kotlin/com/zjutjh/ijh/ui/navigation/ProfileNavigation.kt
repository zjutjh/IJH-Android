package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.ProfileRoute

private const val profileRoute = "profile"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.profileScreen(
    onPopBackStack: () -> Unit,
) {
    composable(profileRoute) {
        ProfileRoute(onNavigateBack = onPopBackStack)
    }
}

fun NavController.navigateToProfile(navOptions: NavOptions?) {
    this.navigate(profileRoute, navOptions)
}