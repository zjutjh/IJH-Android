package com.zjutjh.ijh.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.zjutjh.ijh.ui.viewmodel.ClassScheduleViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val ANIM_DURATION: Int = 300

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IJhNavHost(
    modifier: Modifier = Modifier,
    viewModelProviderFactory: ViewModelProvider.Factory =
        (LocalContext.current as ComponentActivity).defaultViewModelProviderFactory,
    viewModelCreationExtras: CreationExtras =
        (LocalContext.current as ComponentActivity).defaultViewModelCreationExtras,
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: String = homeRoute
) {
    val scope = rememberCoroutineScope()
    val preloadViewModelOwner = remember {
        object : ViewModelStoreOwner {
            override val viewModelStore = ViewModelStore()
            fun clear() = viewModelStore.clear()
        }
    }
    val classScheduleViewModel = MutableStateFlow(null as ClassScheduleViewModel?)

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
                    val vm =
                        ViewModelProvider(
                            preloadViewModelOwner.viewModelStore,
                            viewModelProviderFactory,
                            viewModelCreationExtras
                        )[ClassScheduleViewModel::class.java]

                    classScheduleViewModel.value = vm
                    navController.navigateToClassSchedule(
                        vm,
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
            classScheduleViewModel
        ) {
            navController.popBackStack()
            classScheduleViewModel.value = null
            preloadViewModelOwner.clear()
        }
    }
}
