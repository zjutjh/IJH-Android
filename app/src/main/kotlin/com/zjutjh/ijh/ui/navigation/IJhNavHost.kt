package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.zjutjh.ijh.ui.screen.HomeScreen
import com.zjutjh.ijh.ui.screen.LoginScreen
import com.zjutjh.ijh.ui.screen.Screens

private const val ANIM_DURATION: Int = 200

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IJhNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: String = Screens.HOME.route
) {
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
        composable(Screens.HOME.route) {
            HomeScreen {
                navController.navigate(Screens.LOGIN.route) {
                    launchSingleTop = true
                }
            }

        }
        composable(
            Screens.LOGIN.route,
        ) {
            LoginScreen(onCloseClick = navController::popBackStack) {
                // TODO
                navController.popBackStack(Screens.HOME.route, false)
            }
        }
    }
}
