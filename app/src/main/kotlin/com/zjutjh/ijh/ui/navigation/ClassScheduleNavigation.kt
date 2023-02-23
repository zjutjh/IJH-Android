package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.ClassScheduleRoute
import com.zjutjh.ijh.ui.viewmodel.ClassScheduleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

private const val classScheduleRoute = "classSchedule"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.classScheduleScreen(
    viewModelFlow: StateFlow<ClassScheduleViewModel?>,
    onNavigateBack: () -> Unit,
) {
    composable(classScheduleRoute) {
        val vm by viewModelFlow.collectAsStateWithLifecycle()
        ClassScheduleRoute(
            viewModel = vm!!,
            onNavigateBack = onNavigateBack,
        )
    }
}

suspend fun NavController.navigateToClassSchedule(
    viewModel: ClassScheduleViewModel,
    navOptions: NavOptions? = null
) {
    viewModel.preload()
    delay(100)
    this.navigate(classScheduleRoute, navOptions)
}
