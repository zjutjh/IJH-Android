package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.service.request.LoginBody
import com.zjutjh.ijh.network.service.response.WeJhUserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface WeJhService {

    @POST("user/login")
    suspend fun login(@Body body: LoginBody): Result<WeJhUserResponse>

}