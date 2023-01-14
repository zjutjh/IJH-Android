package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.model.WeJhUser
import kotlinx.coroutines.flow.Flow

interface WeJhUserRepository {
    val userStream: Flow<WeJhUser>

    suspend fun login(username: String, password: String): WeJhUser

    suspend fun logout()

    suspend fun sync()
}