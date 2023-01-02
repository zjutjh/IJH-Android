package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.data.model.WeJhUser
import com.zjutjh.ijh.network.WeJhUserNetworkDataSource
import com.zjutjh.ijh.network.model.NetworkWeJhUser
import javax.inject.Inject

class WeJhUserRepositoryImpl @Inject constructor(
    private val network: WeJhUserNetworkDataSource
) :
    WeJhUserRepository {

    override suspend fun login(username: String, password: String): Result<WeJhUser> =
        network.login(username, password).map { it.asDataModel() }

    private fun NetworkWeJhUser.asDataModel() = WeJhUser(
        this.id,
        this.username,
        this.studentID,
        this.createTime,
        this.phoneNum,
        this.userType,
        this.bind.asDataModel(),
    )

    private fun NetworkWeJhUser.Bind.asDataModel() = WeJhUser.Bind(
        this.lib,
        this.yxy,
        this.zf,
    )

}