package com.zjutjh.ijh.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DividerBottomBar() {
    val bottomInsets = WindowInsets.safeDrawing
        .asPaddingValues()
        .calculateBottomPadding()
    if (bottomInsets.value > 0f) {
        Divider(
            modifier = Modifier
                .padding(bottom = bottomInsets)
                .fillMaxWidth()
        )
    }
}