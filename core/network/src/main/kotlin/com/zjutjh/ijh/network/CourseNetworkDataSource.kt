package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.NetworkClassTable
import com.zjutjh.ijh.network.service.WeJhZfService
import javax.inject.Inject

class CourseNetworkDataSource @Inject constructor(private val service: WeJhZfService) {

    suspend fun getClassTable(year: String, term: String): NetworkClassTable =
        service.getClassTable(
            WeJhZfService.GetClassTableBody(year, term)
        )
}