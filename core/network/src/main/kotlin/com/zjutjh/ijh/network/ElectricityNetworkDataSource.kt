package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.NetworkElectricityBalance
import com.zjutjh.ijh.network.service.WeJhElectricityService
import javax.inject.Inject

class ElectricityNetworkDataSource @Inject constructor(private val weJhElectricityService: WeJhElectricityService) {

    suspend fun getBalance(): NetworkElectricityBalance = weJhElectricityService.getBalance()

}