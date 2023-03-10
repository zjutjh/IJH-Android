package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.model.NetworkWeJhInfo
import retrofit2.http.POST

/**
 * Common WeJH service (without Authorization)
 */
interface WeJhService {
    @POST("info")
    suspend fun getInfo(): NetworkWeJhInfo
}