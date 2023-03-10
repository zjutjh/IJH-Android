package com.zjutjh.ijh.data.repository.mock

import com.zjutjh.ijh.data.repository.WeJhUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.ZonedDateTime

class WeJhUserRepositoryMock : WeJhUserRepository {
    override val userStream: Flow<com.zjutjh.ijh.model.WeJhUser> = flowOf(mockWeJhUser())

    override suspend fun login(username: String, password: String) = Unit

    override suspend fun logout() = Unit

    override suspend fun sync() = Unit

    fun mockWeJhUser(): com.zjutjh.ijh.model.WeJhUser =
        com.zjutjh.ijh.model.WeJhUser(
            uid = 123,
            username = "Info",
            studentId = "20200101",
            createTime = ZonedDateTime.now(),
            phoneNumber = "1810000000",
            userType = 0,
            bind = com.zjutjh.ijh.model.WeJhUser.Bind(
                lib = true,
                zf = true,
                yxy = true,
            ),
        )
}
