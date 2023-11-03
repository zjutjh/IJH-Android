package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.NetworkCampusInfo
import com.zjutjh.ijh.network.service.WeJhBasicService
import javax.inject.Inject

/**
 * Get campus info from network
 */
class CampusNetworkDataSource @Inject constructor(private val weJhBasicService: WeJhBasicService) {
    suspend fun getInfo(): NetworkCampusInfo = weJhBasicService.getInfo()
}