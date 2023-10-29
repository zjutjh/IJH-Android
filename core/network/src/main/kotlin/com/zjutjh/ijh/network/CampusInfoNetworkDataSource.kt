package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.WeJhCampusInfo
import com.zjutjh.ijh.network.service.WeJhBasicService
import javax.inject.Inject

/**
 * Get campus info from network
 */
class CampusInfoNetworkDataSource @Inject constructor(private val weJhBasicService: WeJhBasicService) {
    suspend fun getInfo(): WeJhCampusInfo = weJhBasicService.getInfo()
}