package com.zjutjh.ijh.data.impl

import com.zjutjh.ijh.data.CardInfoRepository
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.toZonedDateTime
import com.zjutjh.ijh.datastore.model.cardOrNull
import com.zjutjh.ijh.model.CardRecord
import com.zjutjh.ijh.network.CardInfoDataSource
import com.zjutjh.ijh.network.model.NetworkCardRecord
import com.zjutjh.ijh.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import java.util.Date
import javax.inject.Inject

class CardInfoRepositoryImpl @Inject constructor(
    private val network: CardInfoDataSource,
    private val local: WeJhPreferenceDataSource
) : CardInfoRepository {

    override val balanceStream: Flow<String?> = local.data.map {
        it.cardOrNull?.balance
    }

    override val lastSyncTimeStream: Flow<ZonedDateTime?> = local.data.map {
        it.cardOrNull?.lastSyncTime?.toZonedDateTime()
    }

    override suspend fun sync() {
        val balance = network.getBalance()
        local.setCard(balance, ZonedDateTime.now())
    }

    override suspend fun getRecord(date: Date): List<CardRecord> =
        network.getRecord(date).map(NetworkCardRecord::asExternalModel)
}