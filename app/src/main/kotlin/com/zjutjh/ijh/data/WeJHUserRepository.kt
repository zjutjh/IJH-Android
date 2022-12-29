package com.zjutjh.ijh.data

import com.zjutjh.ijh.network.Result

interface WeJHUserRepository {

    suspend fun login(username: String, password: String): Result<WeJHUser>

    suspend fun getCurrentUser(): WeJHUser

}