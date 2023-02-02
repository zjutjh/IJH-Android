package com.zjutjh.ijh.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun VerticalScrollableContentBox(
    paddingValues: PaddingValues,
    content: @Composable BoxScope.() -> Unit,
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.TopCenter,
        content = content,
    )
}

@Composable
fun VerticalContentBox(
    paddingValues: PaddingValues,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
        content = content,
    )
}