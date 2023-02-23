package com.zjutjh.ijh.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.zjutjh.ijh.ui.navigation.IJhNavHost

/**
 * App level UI [Composable]
 */
@Composable
fun IJhApp(
    viewModelProviderFactory: ViewModelProvider.Factory,
    viewModelCreationExtras: CreationExtras
) {
    IJhNavHost(
        viewModelProviderFactory = viewModelProviderFactory,
        viewModelCreationExtras = viewModelCreationExtras,
    )
}
