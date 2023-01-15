package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.service.response.ClassTableResult
import com.zjutjh.ijh.network.service.response.WeJhUserResult
import retrofit2.http.POST

interface WeJhAuthorizedService {

    @POST("user/info")
    suspend fun getUserInfo(): WeJhUserResult

    @POST("func/zf/classtable")
    suspend fun getClassTable(): ClassTableResult
}