package com.zjutjh.ijh.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.zjutjh.ijh.ui.screen.ProfileRoute

private const val profileRoute = "profile"

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