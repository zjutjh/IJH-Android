package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.Result
import com.zjutjh.ijh.network.service.request.LoginBody
import com.zjutjh.ijh.network.service.response.UserResult
import retrofit2.http.Body
import retrofit2.http.POST

interface WeJHService {

    @POST("/user/login")
    suspend fun login(@Body body: LoginBody): Result<UserResult>

}