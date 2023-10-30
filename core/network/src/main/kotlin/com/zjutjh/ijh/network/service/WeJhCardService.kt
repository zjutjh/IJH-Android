package com.zjutjh.ijh.network.service

import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.network.model.NetworkCardRecord
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.time.format.DateTimeFormatter

interface WeJhCardService {

    @GET("balance")
    suspend fun getBalance(): String

    @POST("record")
    suspend fun getRecord(@Body queryTime: QueryTime): List<NetworkCardRecord>

    @JsonClass(generateAdapter = true)
    data class QueryTime(
        /**
         * format: yyyyMMdd [DateTimeFormatter.BASIC_ISO_DATE]
         */
        val queryTime: String,
    )
}