package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.ClassScheduleRoute
import com.zjutjh.ijh.ui.viewmodel.ClassScheduleViewModel
import com.zjutjh.ijh.util.ViewModelStoreMappingOwner

private const val classScheduleRoute = "classSchedule"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.classScheduleScreen(
    mappingOwner: ViewModelStoreMappingOwner,
    onNavigateBack: () -> Unit,
) {
    composable(classScheduleRoute) {
        val vm = remember {
            mappingOwner.provider(classScheduleRoute)[ClassScheduleViewModel::class.java]
        }
        ClassScheduleRoute(
            viewModel = vm,
            onNavigateBack = {
                onNavigateBack()
                mappingOwner.remove(classScheduleRoute)
            },
        )
    }
}

suspend fun NavController.navigateToClassSchedule(
    mappingOwner: ViewModelStoreMappingOwner,
    navOptions: NavOptions? = null
) {
    val vm = mappingOwner.provider(classScheduleRoute)[ClassScheduleViewModel::class.java]

    vm.preload()
    this.navigate(classScheduleRoute, navOptions)
}
