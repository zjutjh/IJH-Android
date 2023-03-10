package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.NetworkWeJhUser
import com.zjutjh.ijh.network.service.WeJhUserService
import com.zjutjh.ijh.network.service.request.LoginBody
import javax.inject.Inject

class WeJhUserNetworkDataSource @Inject constructor(
    private val weJhUserService: WeJhUserService
) {

    suspend fun login(username: String, password: String): NetworkWeJhUser =
        weJhUserService.login(LoginBody(username, password)).user

    suspend fun getUserInfo(): NetworkWeJhUser = weJhUserService.getUserInfo().user
}