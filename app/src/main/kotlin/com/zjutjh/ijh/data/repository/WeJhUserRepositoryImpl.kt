package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.datastore.WeJhUserLocalDataSource
import com.zjutjh.ijh.model.WeJhUser
import com.zjutjh.ijh.network.WeJhUserNetworkDataSource
import com.zjutjh.ijh.network.model.asExternalModel
import javax.inject.Inject

class WeJhUserRepositoryImpl @Inject constructor(
    private val network: WeJhUserNetworkDataSource,
    private val local: WeJhUserLocalDataSource,
) : WeJhUserRepository {

    override suspend fun weJhLogin(username: String, password: String): Result<WeJhUser> {
        return network.login(username, password).map {
            it.asExternalModel()
        }.onSuccess {
            local.set(it)
        }
    }

}