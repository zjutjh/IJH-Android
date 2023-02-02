package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.ClassScheduleRoute

private const val classScheduleRoute = "classSchedule"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.classScheduleScreen(
    onNavigateBack: () -> Unit,
) {
    composable(classScheduleRoute) {
        ClassScheduleRoute(
            onNavigateBack = onNavigateBack,
        )
    }
}

fun NavController.navigateToClassSchedule(navOptions: NavOptions? = null) =
    this.navigate(classScheduleRoute, navOptions)