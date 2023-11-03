package com.zjutjh.ijh.network.service

import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.network.model.NetworkWeJhUser
import retrofit2.http.Body
import retrofit2.http.POST

interface WeJhUserService {

    @POST("info")
    suspend fun getUserInfo(): WeJhUserResult

    @POST("login")
    suspend fun login(@Body body: LoginBody): WeJhUserResult

    @POST("login/session")
    suspend fun loginBySession(): WeJhUserResult

    @JsonClass(generateAdapter = true)
    data class LoginBody(
        val username: String,
        val password: String,
    )

    @JsonClass(generateAdapter = true)
    data class WeJhUserResult(
        val user: NetworkWeJhUser,
    )
}