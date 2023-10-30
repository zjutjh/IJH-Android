package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.model.NetworkCardRecord
import com.zjutjh.ijh.network.service.WeJhCardService
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject

class CardInfoDataSource @Inject constructor(private val cardService: WeJhCardService) {

    suspend fun getBalance(): String = cardService.getBalance()

    suspend fun getRecord(date: Date): List<NetworkCardRecord> {
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val queryTime = formatter.format(date.toInstant())
        return cardService.getRecord(
            WeJhCardService.QueryTime(queryTime)
        )
    }
}