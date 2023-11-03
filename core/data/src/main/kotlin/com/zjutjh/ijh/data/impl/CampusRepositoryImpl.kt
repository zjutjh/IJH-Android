package com.zjutjh.ijh.data.impl

import com.zjutjh.ijh.data.CampusRepository
import com.zjutjh.ijh.datastore.IJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.asExternalModel
import com.zjutjh.ijh.datastore.model.campusOrNull
import com.zjutjh.ijh.model.CampusInfo
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.network.CampusNetworkDataSource
import com.zjutjh.ijh.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CampusRepositoryImpl @Inject constructor(
    private val network: CampusNetworkDataSource,
    private val local: IJhPreferenceDataSource
) : CampusRepository {

    override val infoStream: Flow<CampusInfo?> =
        local.data.map { it.campusOrNull?.asExternalModel() }

    override suspend fun sync(): Pair<Int, Term> {
        val weJhInfo = network.getInfo().asExternalModel()
        local.setCampus(weJhInfo)
        return Pair(weJhInfo.year, weJhInfo.term)
    }
}