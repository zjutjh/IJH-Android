package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.model.NetworkCampusInfo
import retrofit2.http.POST

/**
 * Basic WeJH service (no authorization required)
 */
interface WeJhBasicService {
    @POST("info")
    suspend fun getInfo(): NetworkCampusInfo
}