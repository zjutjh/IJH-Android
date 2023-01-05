package com.zjutjh.ijh.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DividerBottomBar() {
    Divider(
        modifier = Modifier
            .padding(
                bottom = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateBottomPadding()
            )
            .fillMaxWidth()
    )
}