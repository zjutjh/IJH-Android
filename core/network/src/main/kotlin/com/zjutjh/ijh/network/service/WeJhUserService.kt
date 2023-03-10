package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.service.request.LoginBody
import com.zjutjh.ijh.network.service.response.WeJhUserResult
import retrofit2.http.Body
import retrofit2.http.POST

interface WeJhUserService {

    @POST("info")
    suspend fun getUserInfo(): WeJhUserResult

    @POST("login")
    suspend fun login(@Body body: LoginBody): WeJhUserResult
}