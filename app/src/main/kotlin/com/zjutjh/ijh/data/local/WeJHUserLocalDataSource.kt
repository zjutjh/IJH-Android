package com.zjutjh.ijh.data.local

import com.zjutjh.ijh.data.WeJHUser
import javax.inject.Inject

class WeJHUserLocalDataSource @Inject constructor() {

    fun getCurrentUser(): WeJHUser = WeJHUser()
}