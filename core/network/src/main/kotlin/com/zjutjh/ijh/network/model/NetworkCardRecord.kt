package com.zjutjh.ijh.network.model

import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.model.CardRecord

/**
 * Card consumption record model
 */
@JsonClass(generateAdapter = true)
data class NetworkCardRecord(
    val address: String,
    val dealTime: String,
    val feeName: String,
    val money: String,
    val serialNo: String,
    val time: String,
    val type: String,
)

fun NetworkCardRecord.asExternalModel() =
    CardRecord(
        address = address,
        dealTime = dealTime,
        feeName = feeName,
        money = money,
        serialNo = serialNo,
        time = time,
        type = type,
    )