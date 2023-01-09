package com.zjutjh.ijh.data.repository.mock

import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.model.WeJhUser
import java.time.ZonedDateTime

class WeJhUserRepositoryMock : WeJhUserRepository {

    override suspend fun weJhLogin(username: String, password: String): Result<WeJhUser> {
        return Result.success(
            WeJhUser(
                uid = 123,
                username = "Info",
                sessionToken = String(),
                sessionExpiresAt = ZonedDateTime.now(),
                studentId = "20200101",
                createTime = ZonedDateTime.now(),
                phoneNumber = "1810000000",
                userType = 0,
                bind = WeJhUser.Bind(
                    lib = true,
                    zf = true,
                    yxy = true,
                ),
            )
        )
    }

}
