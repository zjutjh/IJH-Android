package com.zjutjh.ijh

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.core.animation.Animator
import androidx.core.animation.AnticipateInterpolator
import androidx.core.animation.ObjectAnimator
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zjutjh.ijh.ui.IJhApp
import com.zjutjh.ijh.ui.theme.IJhTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Make system bars inside the application's layout scope
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Set System UI theme
            val isDarkTheme = isSystemInDarkTheme()
            val systemUiController = rememberSystemUiController()

            LaunchedEffect(isDarkTheme, systemUiController) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkTheme,
                )
            }
            IJhTheme(darkTheme = isDarkTheme) {
                IJhApp()
            }
        }

        // Add a callback that's called when the splash screen is animating to
        // the app content.
        splashScreen.setOnExitAnimationListener { splashScreenView ->

            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.TRANSLATION_Y,
                0f,
                splashScreenView.view.height.toFloat()
            )

            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 300L

            // Run animation.
            slideUp.start()

            slideUp.addPauseListener(object : Animator.AnimatorPauseListener {
                override fun onAnimationPause(animation: Animator) {
                    // Remove splash screen
                    splashScreenView.remove()
                }

                override fun onAnimationResume(animation: Animator) {

                }
            })
        }
    }

}
