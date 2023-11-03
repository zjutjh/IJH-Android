package com.zjutjh.ijh.datastore.converter

import com.zjutjh.ijh.datastore.model.LocalWeJhUser
import com.zjutjh.ijh.datastore.model.LocalWeJhUserKt
import com.zjutjh.ijh.datastore.model.localWeJhUser
import com.zjutjh.ijh.model.WeJhUser
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun LocalWeJhUser.asExternalModel() = WeJhUser(
    uid = uid,
    username = username,
    studentId = studentId,
    createTime = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(createTime), ZoneOffset.systemDefault()
    ),
    phoneNumber = phoneNumber,
    userType = this.userType,
    bind = bind.asExternalModel(),
)

fun LocalWeJhUser.Bind.asExternalModel() = WeJhUser.Bind(
    lib = lib,
    yxy = yxy,
    zf = zf,
)

fun WeJhUser.asLocalModel(): LocalWeJhUser =
    localWeJhUser {
        uid = this@asLocalModel.uid
        username = this@asLocalModel.username
        studentId = this@asLocalModel.studentId
        createTime = this@asLocalModel.createTime.toEpochSecond()
        phoneNumber = this@asLocalModel.phoneNumber
        userType = this@asLocalModel.userType
        bind = this@asLocalModel.bind.asLocalModel()
    }

fun WeJhUser.Bind.asLocalModel(): LocalWeJhUser.Bind =
    LocalWeJhUserKt.bind {
        lib = this@asLocalModel.lib
        yxy = this@asLocalModel.yxy
        zf = this@asLocalModel.zf
    }
