package com.zjutjh.ijh.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * WeJH system Info
 */
@JsonClass(generateAdapter = true)
data class WeJhInfo(
    @Json(name = "is_begin")
    val isBegin: Boolean,
    val term: String,
    val termYear: String,
    val termStartDate: String,
    val time: String,
    val week: Int,
    val schoolBusUrl: String,
)