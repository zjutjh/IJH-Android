package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.service.request.LoginBody
import com.zjutjh.ijh.network.service.response.WeJhUserResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Common WeJH service (without Authorization)
 */
interface WeJhService {

    @POST("user/login")
    suspend fun login(@Body body: LoginBody): Response<WeJhUserResult>
}