package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.model.WeJhUser

interface WeJhUserRepository {

    suspend fun weJhLogin(username: String, password: String): Result<WeJhUser>

}