package com.zjutjh.ijh.mock

import com.zjutjh.ijh.data.WeJHUser
import com.zjutjh.ijh.data.WeJHUserRepository
import com.zjutjh.ijh.network.Result

class WeJHUserRepositoryMock : WeJHUserRepository {
    override suspend fun login(username: String, password: String): Result<WeJHUser> = Result.Ok(
        WeJHUser()
    )

    override suspend fun getCurrentUser() = WeJHUser()
}