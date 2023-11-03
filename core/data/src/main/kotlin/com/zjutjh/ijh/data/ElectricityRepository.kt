package com.zjutjh.ijh.data

import com.zjutjh.ijh.model.ElectricityBalance

interface ElectricityRepository {

    suspend fun getBalance(): ElectricityBalance
}