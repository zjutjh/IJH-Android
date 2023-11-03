package com.zjutjh.ijh.data.impl

import com.zjutjh.ijh.data.ElectricityRepository
import com.zjutjh.ijh.model.ElectricityBalance
import com.zjutjh.ijh.network.ElectricityNetworkDataSource
import com.zjutjh.ijh.network.model.asExternalModel
import javax.inject.Inject

class ElectricityRepositoryImpl @Inject constructor(
    private val network: ElectricityNetworkDataSource
) : ElectricityRepository {

    override suspend fun getBalance(): ElectricityBalance = network.getBalance().asExternalModel()
}