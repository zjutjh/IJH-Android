package com.zjutjh.ijh.data

import com.zjutjh.ijh.model.CardRecord
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import java.util.Date

interface CardRepository {

    /**
     * balance (Unit: CNY) in string
     */
    val balanceStream: Flow<String?>

    val lastSyncTimeStream: Flow<ZonedDateTime?>

    suspend fun sync()

    suspend fun getRecord(date: Date): List<CardRecord>
}