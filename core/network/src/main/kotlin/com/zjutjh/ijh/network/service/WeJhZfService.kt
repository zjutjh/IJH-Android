package com.zjutjh.ijh.network.service

import com.zjutjh.ijh.network.model.ClassTable
import retrofit2.http.POST

interface WeJhZfService {

    @POST("classtable")
    suspend fun getClassTable(): ClassTable
}