package com.zjutjh.ijh.model

import androidx.compose.runtime.Stable

@Stable
data class ElectricityBalance(
    val total: Float,
    val totalAmount: Float,
    val subsidy: Float,
    val subsidyAmount: Float,
    val surplus: Float,
    val surplusAmount: Float
)