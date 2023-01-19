package com.zjutjh.ijh.data.model

import com.zjutjh.ijh.datastore.model.LocalWeJhUser
import com.zjutjh.ijh.datastore.model.LocalWeJhUserKt
import com.zjutjh.ijh.datastore.model.localWeJhUser
import com.zjutjh.ijh.network.model.NetworkWeJhUser
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


fun NetworkWeJhUser.asLocalModel(): LocalWeJhUser =
    localWeJhUser {
        uid = this@asLocalModel.id
        username = this@asLocalModel.username
        this.sessionToken = this@asLocalModel.sessionToken!!
        this.sessionExpiresAt = this@asLocalModel.sessionExpiresAt!!.toEpochSecond()
        studentId = this@asLocalModel.studentId
        createTime = ZonedDateTime.parse(
            this@asLocalModel.createTime,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ).toEpochSecond()
        phoneNumber = this@asLocalModel.phoneNumber
        userType = this@asLocalModel.userType
        bind = this@asLocalModel.bind.asLocalModel()
    }

fun NetworkWeJhUser.asLocalModel(
    sessionToken: String,
    sessionExpiresAt: Long,
): LocalWeJhUser =
    localWeJhUser {
        uid = this@asLocalModel.id
        username = this@asLocalModel.username
        this.sessionToken = sessionToken
        this.sessionExpiresAt = sessionExpiresAt
        studentId = this@asLocalModel.studentId
        createTime = ZonedDateTime.parse(
            this@asLocalModel.createTime,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ).toEpochSecond()
        phoneNumber = this@asLocalModel.phoneNumber
        userType = this@asLocalModel.userType
        bind = this@asLocalModel.bind.asLocalModel()
    }

fun NetworkWeJhUser.Bind.asLocalModel(): LocalWeJhUser.Bind =
    LocalWeJhUserKt.bind {
        lib = this@asLocalModel.lib
        yxy = this@asLocalModel.yxy
        zf = this@asLocalModel.zf
    }