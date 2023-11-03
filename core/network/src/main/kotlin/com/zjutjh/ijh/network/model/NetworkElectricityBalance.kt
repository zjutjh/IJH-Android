package com.zjutjh.ijh.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkElectricityBalance(
    val areaID: String,
    val buildingCode: String,
    val displayRoomName: String,
    val floorCode: String,
    val mdName: String,
    val mdType: String,
    val roomCode: String,
    val roomStatus: String,
    val schoolCode: String,
    val soc: Double,
    val socAmount: Double,
    val subsidy: Long,
    val subsidyAmount: Long,
    val surplus: Double,
    val surplusAmount: Double
)