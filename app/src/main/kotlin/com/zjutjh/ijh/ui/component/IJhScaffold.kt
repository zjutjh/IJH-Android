package com.zjutjh.ijh.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IJhScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable (TopAppBarScrollBehavior) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.(PaddingValues) -> Unit,
) {
    val navigationBarsInsets = WindowInsets.navigationBars
    val windowInset = WindowInsets.safeDrawing.exclude(navigationBarsInsets)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { topBar(scrollBehavior) },
        snackbarHost = snackbarHost,
        contentWindowInsets = windowInset,
        content = {
            VerticalContentBox(
                paddingValues = it,
                content = { content(navigationBarsInsets.asPaddingValues()) },
            )
        },
    )
}