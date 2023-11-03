package com.zjutjh.ijh.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.model.ElectricityBalance

@JsonClass(generateAdapter = true)
data class NetworkElectricityBalance(
    @Json(name = "area_id")
    val areaId: String,
    @Json(name = "building_code")
    val buildingCode: String,
    @Json(name = "display_room_name")
    val displayRoomName: String,
    @Json(name = "floor_code")
    val floorCode: String,
    @Json(name = "md_name")
    val mdName: String,
    @Json(name = "md_type")
    val mdType: String,
    @Json(name = "room_code")
    val roomCode: String,
    @Json(name = "room_status")
    val roomStatus: String,
    @Json(name = "school_code")
    val schoolCode: String,
    val soc: Float,
    @Json(name = "soc_amount")
    val socAmount: Float,
    val subsidy: Float,
    @Json(name = "subsidy_amount")
    val subsidyAmount: Float,
    val surplus: Float,
    @Json(name = "surplus_amount")
    val surplusAmount: Float
)

fun NetworkElectricityBalance.asExternalModel(): ElectricityBalance =
    ElectricityBalance(
        total = soc,
        totalAmount = socAmount,
        subsidy = subsidy,
        subsidyAmount = subsidyAmount,
        surplus = surplus,
        surplusAmount = surplusAmount
    )