package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.model.NetworkElectricityBalance
import retrofit2.http.GET

interface WeJhElectricityService {

    @GET("balance")
    suspend fun getBalance(): NetworkElectricityBalance
}