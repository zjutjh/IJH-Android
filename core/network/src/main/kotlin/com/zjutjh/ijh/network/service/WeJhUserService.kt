package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.service.response.WeJhUserResult
import retrofit2.http.POST

interface WeJhUserService {

    @POST("user/info")
    suspend fun getUserInfo(): WeJhUserResult
}