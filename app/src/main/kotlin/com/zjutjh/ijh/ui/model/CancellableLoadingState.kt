package com.zjutjh.ijh.ui.model

import androidx.compose.runtime.Stable

@Stable
enum class CancellableLoadingState {
    READY, LOADING, CANCELLABLE, PENDING;

    fun isLoading() = this == LOADING || this == CANCELLABLE
}