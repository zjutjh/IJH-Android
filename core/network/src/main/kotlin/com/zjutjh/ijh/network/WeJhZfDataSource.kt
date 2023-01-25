package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.ClassTable
import com.zjutjh.ijh.network.service.WeJhZfService
import com.zjutjh.ijh.network.service.request.GetClassTableBody
import javax.inject.Inject

/**
 * Data source proxy of ZF service
 */
class WeJhZfDataSource @Inject constructor(private val service: WeJhZfService) {

    suspend fun getClassTable(year: String, term: String): ClassTable = service.getClassTable(
        GetClassTableBody(year, term)
    )
}