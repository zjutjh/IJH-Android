package com.zjutjh.ijh.data.converter

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