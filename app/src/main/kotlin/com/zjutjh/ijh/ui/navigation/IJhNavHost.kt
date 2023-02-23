package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.zjutjh.ijh.ui.viewmodel.ClassScheduleViewModel
import kotlinx.coroutines.launch

private const val ANIM_DURATION: Int = 300

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IJhNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
    classScheduleViewModel: ClassScheduleViewModel = viewModel(),
    startDestination: String = homeRoute
) {
    val scope = rememberCoroutineScope()

    AnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIn(tween(ANIM_DURATION)) {
                IntOffset(x = it.width / 4, y = 0)
            } + fadeIn(tween(ANIM_DURATION))
        },
        exitTransition = {
            slideOut(tween(ANIM_DURATION)) {
                IntOffset(x = -it.width / 4, y = 0)
            } + fadeOut(tween(ANIM_DURATION))
        },
        popEnterTransition = {
            slideIn(tween(ANIM_DURATION)) {
                IntOffset(x = -it.width / 4, y = 0)
            } + fadeIn()
        },
        popExitTransition = {
            slideOut(tween(ANIM_DURATION)) {
                IntOffset(x = it.width / 4, y = 0)
            } + fadeOut(tween(ANIM_DURATION))
        }
    ) {
        homeScreen(
            onNavigateToLogin = {
                navController.navigateToLogin(
                    navOptions { launchSingleTop = true }
                )
            },
            onNavigateToProfile = {
                navController.navigateToProfile(
                    navOptions { launchSingleTop = true }
                )
            },
            onNavigateToClassSchedule = {
                scope.launch {
                    navController.navigateToClassSchedule(
                        classScheduleViewModel,
                        navOptions { launchSingleTop = true }
                    )
                }
            }
        )

        loginScreen(
            navController::popBackStack,
            navController::popUpToHome,
        )

        profileScreen(
            navController::popBackStack
        )

        classScheduleScreen(
            classScheduleViewModel,
            navController::popBackStack
        )
    }
}
