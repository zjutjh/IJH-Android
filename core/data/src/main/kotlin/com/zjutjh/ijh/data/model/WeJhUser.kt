package com.zjutjh.ijh.data.model

import com.zjutjh.ijh.datastore.model.WeJhPreference
import com.zjutjh.ijh.datastore.model.WeJhPreferenceKt
import com.zjutjh.ijh.network.model.NetworkWeJhUser
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun NetworkWeJhUser.asLocalModel(): WeJhPreference.User =
    WeJhPreferenceKt.user {
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

fun NetworkWeJhUser.Bind.asLocalModel(): WeJhPreference.User.Bind =
    WeJhPreferenceKt.UserKt.bind {
        lib = this@asLocalModel.lib
        yxy = this@asLocalModel.yxy
        zf = this@asLocalModel.zf
    }