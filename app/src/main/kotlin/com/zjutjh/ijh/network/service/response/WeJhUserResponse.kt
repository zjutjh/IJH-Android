package com.zjutjh.ijh.network.service.response

import com.zjutjh.ijh.network.model.NetworkWeJhUser

data class WeJhUserResponse(
    val user: NetworkWeJhUser = NetworkWeJhUser(),
)