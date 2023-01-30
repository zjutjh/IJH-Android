package com.zjutjh.ijh.data.repository

import android.util.Log
import com.zjutjh.ijh.data.model.asLocalModel
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.asExternalModel
import com.zjutjh.ijh.datastore.model.infoOrNull
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

    override suspend fun sync() {
        val weJhInfo = network.getInfo()
        Log.i("WeJhInfoSync", weJhInfo.asExternalModel().toString())
        local.setInfo(weJhInfo.asLocalModel())
    }
}