package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.data.model.WeJhUser

interface WeJhUserRepository {

    suspend fun login(username: String, password: String): Result<WeJhUser>

}