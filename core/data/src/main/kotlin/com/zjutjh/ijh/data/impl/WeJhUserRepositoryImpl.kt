package com.zjutjh.ijh.data.impl

import com.zjutjh.ijh.data.WeJhUserRepository
import com.zjutjh.ijh.data.converter.asLocalModel
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.asExternalModel
import com.zjutjh.ijh.datastore.model.sessionOrNull
import com.zjutjh.ijh.datastore.model.userOrNull
import com.zjutjh.ijh.model.Session
import com.zjutjh.ijh.model.WeJhUser
import com.zjutjh.ijh.network.WeJhUserNetworkDataSource
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

    override val sessionStream: Flow<Session?> =
        local.data.map {
            it.sessionOrNull?.asExternalModel()
        }

    override suspend fun login(username: String, password: String) {
        val user = network.login(username, password)
        local.setUser(user.asLocalModel())
    }

    override suspend fun renewSession() {
        val user = network.loginBySession()
        local.setUser(user.asLocalModel())
    }

    override suspend fun logout() {
        local.deleteSession()
        local.deleteUser()
    }

    override suspend fun sync() {
        val user = network.getUserInfo()
        local.setUser(user.asLocalModel())
    }
}