package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.NetworkWeJhInfo
import com.zjutjh.ijh.network.service.WeJhService
import javax.inject.Inject

/**
 * WeJH system relevant data source
 */
class WeJhSystemDataSource @Inject constructor(private val weJhService: WeJhService) {
    suspend fun getInfo(): NetworkWeJhInfo = weJhService.getInfo()
}