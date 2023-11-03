package com.zjutjh.ijh.data.impl

import com.zjutjh.ijh.data.CardRepository
import com.zjutjh.ijh.datastore.IJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.toZonedDateTime
import com.zjutjh.ijh.datastore.model.cardOrNull
import com.zjutjh.ijh.model.CardRecord
import com.zjutjh.ijh.network.CardNetworkDataSource
import com.zjutjh.ijh.network.model.NetworkCardRecord
import com.zjutjh.ijh.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import java.util.Date
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val network: CardNetworkDataSource,
    private val local: IJhPreferenceDataSource
) : CardRepository {

    override val balanceStream: Flow<String?> = local.data.map {
        it.cardOrNull?.balance
    }

    override val lastSyncTimeStream: Flow<ZonedDateTime?> = local.data.map {
        it.cardOrNull?.syncTime?.toZonedDateTime()
    }

    override suspend fun sync() {
        val balance = network.getBalance()
        local.setCard(balance, ZonedDateTime.now())
    }

    override suspend fun getRecord(date: Date): List<CardRecord> =
        network.getRecord(date).map(NetworkCardRecord::asExternalModel)
}