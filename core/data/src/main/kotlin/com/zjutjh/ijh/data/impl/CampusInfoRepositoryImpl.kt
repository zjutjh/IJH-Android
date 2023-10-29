package com.zjutjh.ijh.data.impl

import com.zjutjh.ijh.data.CampusInfoRepository
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.asExternalModel
import com.zjutjh.ijh.datastore.model.infoOrNull
import com.zjutjh.ijh.model.CampusInfo
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.network.CampusInfoNetworkDataSource
import com.zjutjh.ijh.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CampusInfoRepositoryImpl @Inject constructor(
    private val network: CampusInfoNetworkDataSource,
    private val local: WeJhPreferenceDataSource
) : CampusInfoRepository {

    override val infoStream: Flow<CampusInfo?> = local.data.map { it.infoOrNull?.asExternalModel() }

    override suspend fun sync(): Pair<Int, Term> {
        val weJhInfo = network.getInfo().asExternalModel()
        local.setInfo(weJhInfo)
        return Pair(weJhInfo.year, weJhInfo.term)
    }
}