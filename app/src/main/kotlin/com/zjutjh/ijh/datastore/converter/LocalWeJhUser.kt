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
    sessionToken = sessionToken,
    sessionExpiresAt = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(sessionExpiresAt), ZoneOffset.UTC
    ),
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

fun WeJhUser.asInternalModel(): LocalWeJhUser =
    localWeJhUser {
        uid = this@asInternalModel.uid
        username = this@asInternalModel.username
        sessionToken = this@asInternalModel.sessionToken
        sessionExpiresAt = this@asInternalModel.sessionExpiresAt.toEpochSecond()
        studentId = this@asInternalModel.studentId
        createTime = this@asInternalModel.createTime.toEpochSecond()
        phoneNumber = this@asInternalModel.phoneNumber
        userType = this@asInternalModel.userType
        bind = this@asInternalModel.bind.asInternalModel()
    }

fun WeJhUser.Bind.asInternalModel(): LocalWeJhUser.Bind =
    LocalWeJhUserKt.bind {
        lib = this@asInternalModel.lib
        yxy = this@asInternalModel.yxy
        zf = this@asInternalModel.zf
    }