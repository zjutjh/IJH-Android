package com.zjutjh.ijh

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.animation.Animator
import androidx.core.animation.ObjectAnimator
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.zjutjh.ijh.ui.IJhApp
import com.zjutjh.ijh.ui.theme.IJhTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        // Add a callback that's called when the splash screen is animating to
        // the app content.
        splashScreen.setOnExitAnimationListener { splashScreenView ->

            val animator = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.TRANSLATION_Y,
                0f,
                splashScreenView.view.height.toFloat(),
            )

            animator.duration = 300L

            animator.doOnEnd {
                splashScreenView.remove()
            }

            // Run animation.
            animator.start()
        }

        super.onCreate(savedInstanceState)

        // Make system bars inside the application's layout scope
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            IJhTheme {
                IJhApp()
            }
        }
    }

    private fun ObjectAnimator.doOnEnd(onEnd: (Animator) -> Unit) =
        this.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animation: Animator) = onEnd(animation)
        })

    private abstract class AnimatorEndListener :
        Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) = Unit

        abstract override fun onAnimationEnd(animation: Animator)

        override fun onAnimationCancel(animation: Animator) = Unit

        override fun onAnimationRepeat(animation: Animator) = Unit
    }
}
