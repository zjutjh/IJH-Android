package com.zjutjh.ijh.ui.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.zjutjh.ijh.ui.screen.HomeScreen
import com.zjutjh.ijh.ui.screen.LoginScreen
import com.zjutjh.ijh.ui.screen.Screens


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
        startDestination = startDestination
    ) {
        composable(
            Screens.HOME.route,
        ) {
            HomeScreen {
                navController.navigate(Screens.LOGIN.route) {
                    launchSingleTop = true
                }
            }
        }
        composable(
            Screens.LOGIN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                ) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                ) + fadeOut()
            }
        ) {
            LoginScreen {
                navController.popBackStack()
            }
        }
    }
}
