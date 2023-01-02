package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.NetworkWeJhUser
import com.zjutjh.ijh.network.service.WeJhService
import com.zjutjh.ijh.network.service.request.LoginBody
import javax.inject.Inject

class WeJhUserNetworkDataSource @Inject constructor(private val weJhService: WeJhService) {

    suspend fun login(username: String, password: String): Result<NetworkWeJhUser> =
        weJhService.login(LoginBody(username, password)).map { it.user }

}