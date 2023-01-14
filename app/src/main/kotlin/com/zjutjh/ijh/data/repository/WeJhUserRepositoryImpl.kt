package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.datastore.WeJhUserLocalDataSource
import com.zjutjh.ijh.datastore.converter.asExternalModel
import com.zjutjh.ijh.datastore.model.LocalWeJhUser
import com.zjutjh.ijh.model.WeJhUser
import com.zjutjh.ijh.network.WeJhUserNetworkDataSource
import com.zjutjh.ijh.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeJhUserRepositoryImpl @Inject constructor(
    private val network: WeJhUserNetworkDataSource,
    private val local: WeJhUserLocalDataSource,
) : WeJhUserRepository {

    override val userStream: Flow<WeJhUser> = local.user.map(LocalWeJhUser::asExternalModel)

    override suspend fun login(username: String, password: String): WeJhUser {
        val user = network.login(username, password).asExternalModel()
        local.set(user)
        return user
    }

    override suspend fun logout() = local.delete()

    override suspend fun sync() {
        val user = network.getUserInfo()
        local.update(user)
    }
}