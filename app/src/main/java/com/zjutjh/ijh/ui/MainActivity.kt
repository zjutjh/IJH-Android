package com.zjutjh.ijh.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zjutjh.ijh.ui.theme.IJHTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make system bars inside the application's layout scope
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Set System UI theme
            val isDarkTheme = isSystemInDarkTheme()
            val systemUiController = rememberSystemUiController()

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkTheme,
                )
            }
            IJHTheme(darkTheme = isDarkTheme) {
                IJHApp()
            }
        }
    }
}
