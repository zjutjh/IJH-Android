package com.zjutjh.ijh.data.remote

import com.zjutjh.ijh.data.WeJHUser
import com.zjutjh.ijh.network.Result
import com.zjutjh.ijh.network.service.WeJHAuthorizedService
import com.zjutjh.ijh.network.service.WeJHService
import com.zjutjh.ijh.network.service.request.LoginBody
import javax.inject.Inject

class WeJHUserNetworkDataSource @Inject constructor(
    private val weJHService: WeJHService,
    private val weJHAuthorizedService: WeJHAuthorizedService
) {

    suspend fun login(username: String, password: String): Result<WeJHUser> {
        when (val result = weJHService.login(LoginBody(username, password))) {
            is Result.Ok -> {
                val data = result.data.user
                val user = WeJHUser(
                    userID = data.id,
                    studentID = data.studentID,
                    createTime = data.createTime,
                    phoneNum = data.phoneNum,
                    username = data.username,
                    userType = data.userType,
                    bindLib = data.bind.lib,
                    bindYxy = data.bind.yxy,
                    bindZf = data.bind.zf,
                )
                return Result.Ok(user)
            }
            is Result.Err -> {
                return result
            }
        }
    }

}