package com.zjutjh.ijh.ui

import androidx.compose.runtime.Composable
import com.zjutjh.ijh.ui.navigation.IJhNavHost
import com.zjutjh.ijh.util.ViewModelStoreMappingOwner

/**
 * App level UI [Composable]
 */
@Composable
fun IJhApp(
    sharedViewModelStoreOwner: ViewModelStoreMappingOwner,
) {
    IJhNavHost(
        sharedViewModelStoreOwner = sharedViewModelStoreOwner,
    )
}
