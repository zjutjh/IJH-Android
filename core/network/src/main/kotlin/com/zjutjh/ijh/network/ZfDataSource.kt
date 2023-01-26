package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.ZfClassTable
import com.zjutjh.ijh.network.service.WeJhZfService
import com.zjutjh.ijh.network.service.request.GetClassTableBody
import javax.inject.Inject

/**
 * Data source proxy of ZF service
 */
class ZfDataSource @Inject constructor(private val service: WeJhZfService) {

    suspend fun getClassTable(year: String, term: String): ZfClassTable = service.getClassTable(
        GetClassTableBody(year, term)
    )
}