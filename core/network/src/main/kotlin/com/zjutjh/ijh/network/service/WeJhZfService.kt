package com.zjutjh.ijh.network.service

import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.network.model.NetworkClassTable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * ZF service provided by WeJH
 */
interface WeJhZfService {

    @POST("classtable")
    suspend fun getClassTable(@Body body: GetClassTableBody): NetworkClassTable

    @JsonClass(generateAdapter = true)
    data class GetClassTableBody(
        val year: String,
        val term: String,
    )
}