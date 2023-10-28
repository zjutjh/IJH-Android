package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.zjutjh.ijh.util.ViewModelStoreMappingOwner
import kotlinx.coroutines.launch

private const val ANIM_DURATION: Int = 300

@Composable
fun IJhNavHost(
    modifier: Modifier = Modifier,
    sharedViewModelStoreOwner: ViewModelStoreMappingOwner,
    navController: NavHostController = rememberNavController(),
    startDestination: String = homeRoute
) {
    val scope = rememberCoroutineScope()

    NavHost(
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
            onNavigateToCalendar = {
                scope.launch {
                    navController.navigateToCourseCalendar(
                        sharedViewModelStoreOwner,
                        navOptions { launchSingleTop = true }
                    )
                }
            },
            onNavigateToAbout = {
                navController.navigateToAbout(
                    navOptions { launchSingleTop = true }
                )
            }
        )

        loginScreen(
            navController::popBackStack,
            navController::popUpToHome,
        )

        profileScreen(
            navController::popBackStack
        )

        courseCalendarScreen(
            sharedViewModelStoreOwner,
            navController::popBackStack,
        )

        aboutScreen(
            navController::popBackStack
        )
    }
}
