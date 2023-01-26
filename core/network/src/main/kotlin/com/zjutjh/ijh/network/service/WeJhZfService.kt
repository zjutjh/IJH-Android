package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.model.ZfClassTable
import com.zjutjh.ijh.network.service.request.GetClassTableBody
import retrofit2.http.Body
import retrofit2.http.POST

interface WeJhZfService {

    @POST("classtable")
    suspend fun getClassTable(@Body body: GetClassTableBody): ZfClassTable
}