package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.ClassScheduleRoute
import com.zjutjh.ijh.ui.viewmodel.ClassScheduleViewModel

private const val classScheduleRoute = "classSchedule"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.classScheduleScreen(
    viewModel: ClassScheduleViewModel,
    onNavigateBack: () -> Unit,
) {
    composable(classScheduleRoute) {
        ClassScheduleRoute(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack,
        )
    }
}

suspend fun NavController.navigateToClassSchedule(
    viewModel: ClassScheduleViewModel,
    navOptions: NavOptions? = null
) {
    viewModel.preload()
    this.navigate(classScheduleRoute, navOptions)
}
