package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.data.model.asLocalModel
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.asExternalModel
import com.zjutjh.ijh.datastore.model.userOrNull
import com.zjutjh.ijh.model.WeJhUser
import com.zjutjh.ijh.network.WeJhUserNetworkDataSource
import com.zjutjh.ijh.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeJhUserRepositoryImpl @Inject constructor(
    private val network: WeJhUserNetworkDataSource,
    private val local: WeJhPreferenceDataSource,
) : WeJhUserRepository {

    override val userStream: Flow<WeJhUser?> =
        local.data.map {
            it.userOrNull?.asExternalModel()
        }

    override suspend fun login(username: String, password: String): WeJhUser {
        val user = network.login(username, password)
        local.setUser(user.asLocalModel())
        return user.asExternalModel()
    }

    override suspend fun logout() = local.deleteUser()

    override suspend fun sync() {
        val user = network.getUserInfo()
        local.updateUser {
            user.asLocalModel(it.sessionToken, it.sessionExpirationTime)
        }
    }
}