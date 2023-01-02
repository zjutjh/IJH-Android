package com.zjutjh.ijh.data.repository.mock

import com.zjutjh.ijh.data.model.WeJhUser
import com.zjutjh.ijh.data.repository.WeJhUserRepository

class WeJhUserRepositoryMock : WeJhUserRepository {

    override suspend fun login(username: String, password: String): Result<WeJhUser> =
        Result.success(
            WeJhUser(
                uid = 123,
                username = "Info",
                studentId = "20200101",
                createTime = "2020-01-01 10:00:00",
                phoneNum = "1810000000",
                userType = 0,
                bind = WeJhUser.Bind(
                    lib = true,
                    zf = true,
                    yxy = true,
                ),
            )
        )
}