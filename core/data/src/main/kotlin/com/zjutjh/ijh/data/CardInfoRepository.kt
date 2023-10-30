package com.zjutjh.ijh.data

import com.zjutjh.ijh.model.CardRecord
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import java.util.Date

interface CardInfoRepository {

    /**
     * [Pair]: balance (Unit: CNY) in string, last sync time
     */
    val balanceStream: Flow<Pair<String, ZonedDateTime>?>

    suspend fun sync()

    suspend fun getRecord(date: Date): List<CardRecord>
}