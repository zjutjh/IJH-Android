package com.zjutjh.ijh.model

import androidx.compose.runtime.Stable

@Stable
data class CardRecord(
    val address: String,
    val dealTime: String,
    val feeName: String,
    val money: String,
    val serialNo: String,
    val time: String,
    val type: String,
)