package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.NetworkWeJhUser
import com.zjutjh.ijh.network.service.WeJhUserService
import javax.inject.Inject

class WeJhUserNetworkDataSource @Inject constructor(
    private val weJhUserService: WeJhUserService
) {

    suspend fun login(username: String, password: String): NetworkWeJhUser =
        weJhUserService.login(WeJhUserService.LoginBody(username, password)).user

    suspend fun loginBySession(): NetworkWeJhUser =
        weJhUserService.loginBySession().user

    suspend fun getUserInfo(): NetworkWeJhUser = weJhUserService.getUserInfo().user
}