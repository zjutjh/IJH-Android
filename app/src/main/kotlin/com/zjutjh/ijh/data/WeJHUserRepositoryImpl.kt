package com.zjutjh.ijh.data

import com.zjutjh.ijh.data.local.WeJHUserLocalDataSource
import com.zjutjh.ijh.data.remote.WeJHUserNetworkDataSource
import com.zjutjh.ijh.network.Result
import javax.inject.Inject

class WeJHUserRepositoryImpl @Inject constructor(
    private val localDataSource: WeJHUserLocalDataSource,
    private val remoteDataSource: WeJHUserNetworkDataSource
) : WeJHUserRepository {

    override suspend fun login(username: String, password: String): Result<WeJHUser> =
        remoteDataSource.login(username, password)

    override suspend fun getCurrentUser(): WeJHUser {
        TODO("Not yet implemented")
    }
}