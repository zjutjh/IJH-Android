package com.zjutjh.ijh.data.repository.mock

import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.model.WeJhUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.ZonedDateTime

class WeJhUserRepositoryMock : WeJhUserRepository {
    override val userStream: Flow<WeJhUser> = flowOf(mockWeJhUser())

    override suspend fun login(username: String, password: String): WeJhUser = mockWeJhUser()

    override suspend fun logout() = Unit

    override suspend fun sync() = Unit

    fun mockWeJhUser(): WeJhUser =
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
}
