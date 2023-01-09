package com.zjutjh.ijh.network.service.response

import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.network.model.NetworkWeJhUser

@JsonClass(generateAdapter = true)
data class WeJhUserResult(
    val user: NetworkWeJhUser,
)