package com.zjutjh.ijh.network.service.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetClassTableBody(
    val year: String,
    val term: String,
)