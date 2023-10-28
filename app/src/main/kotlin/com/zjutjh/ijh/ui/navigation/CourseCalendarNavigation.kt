package com.zjutjh.ijh.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.zjutjh.ijh.ui.screen.CourseCalendarRoute
import com.zjutjh.ijh.ui.viewmodel.CourseCalendarViewModel
import com.zjutjh.ijh.util.ViewModelStoreMappingOwner

private const val COURSE_CALENDAR_ROUTE = "courseCalendar"

fun NavGraphBuilder.courseCalendarScreen(
    mappingOwner: ViewModelStoreMappingOwner,
    onNavigateBack: () -> Unit,
) {
    fun onBack() {
        mappingOwner.remove(COURSE_CALENDAR_ROUTE)
        onNavigateBack()
    }

    composable(COURSE_CALENDAR_ROUTE) {
        val vm = remember {
            mappingOwner.provider(COURSE_CALENDAR_ROUTE)[CourseCalendarViewModel::class.java]
        }
        CourseCalendarRoute(
            viewModel = vm,
            onNavigateBack = ::onBack,
        )
        BackHandler(onBack = ::onBack)
    }
}

suspend fun NavController.navigateToCourseCalendar(
    mappingOwner: ViewModelStoreMappingOwner,
    navOptions: NavOptions? = null
) {
    val vm = mappingOwner.provider(COURSE_CALENDAR_ROUTE)[CourseCalendarViewModel::class.java]

    vm.preload()
    this.navigate(COURSE_CALENDAR_ROUTE, navOptions)
}
