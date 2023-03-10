package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.model.Session
import com.zjutjh.ijh.model.WeJhUser
import kotlinx.coroutines.flow.Flow

interface WeJhUserRepository {
    val userStream: Flow<WeJhUser?>

    val sessionStream: Flow<Session?>

    suspend fun login(username: String, password: String)

    suspend fun logout()

    suspend fun sync()
}