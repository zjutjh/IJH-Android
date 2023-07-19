package com.zjutjh.ijh.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.zjutjh.ijh.ui.screen.ClassScheduleRoute
import com.zjutjh.ijh.ui.viewmodel.ClassScheduleViewModel
import com.zjutjh.ijh.util.ViewModelStoreMappingOwner

private const val CLASS_SCHEDULE_ROUTE = "classSchedule"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.classScheduleScreen(
    mappingOwner: ViewModelStoreMappingOwner,
    onNavigateBack: () -> Unit,
) {
    fun onBack() {
        mappingOwner.remove(CLASS_SCHEDULE_ROUTE)
        onNavigateBack()
    }

    composable(CLASS_SCHEDULE_ROUTE) {
        val vm = remember {
            mappingOwner.provider(CLASS_SCHEDULE_ROUTE)[ClassScheduleViewModel::class.java]
        }
        ClassScheduleRoute(
            viewModel = vm,
            onNavigateBack = ::onBack,
        )
        BackHandler(onBack = ::onBack)
    }
}

suspend fun NavController.navigateToClassSchedule(
    mappingOwner: ViewModelStoreMappingOwner,
    navOptions: NavOptions? = null
) {
    val vm = mappingOwner.provider(CLASS_SCHEDULE_ROUTE)[ClassScheduleViewModel::class.java]

    vm.preload()
    this.navigate(CLASS_SCHEDULE_ROUTE, navOptions)
}
