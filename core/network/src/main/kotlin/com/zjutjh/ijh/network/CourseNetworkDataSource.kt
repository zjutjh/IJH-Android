package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.ZfClassTable
import com.zjutjh.ijh.network.service.WeJhZfService
import com.zjutjh.ijh.network.service.request.GetClassTableBody
import javax.inject.Inject

class CourseNetworkDataSource @Inject constructor(private val service: WeJhZfService) {

    suspend fun getZfClassTable(year: String, term: String): ZfClassTable = service.getClassTable(
        GetClassTableBody(year, term)
    )
}