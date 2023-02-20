package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.asExternalModel
import com.zjutjh.ijh.datastore.model.infoOrNull
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.model.WeJhInfo
import com.zjutjh.ijh.network.WeJhSystemDataSource
import com.zjutjh.ijh.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeJhInfoRepositoryImpl @Inject constructor(
    private val network: WeJhSystemDataSource,
    private val local: WeJhPreferenceDataSource
) : WeJhInfoRepository {

    override val infoStream: Flow<WeJhInfo?> = local.data.map { it.infoOrNull?.asExternalModel() }

    override suspend fun sync(): Pair<Int, Term> {
        val weJhInfo = network.getInfo().asExternalModel()
        local.setInfo(weJhInfo)
        return Pair(weJhInfo.year, weJhInfo.term)
    }
}