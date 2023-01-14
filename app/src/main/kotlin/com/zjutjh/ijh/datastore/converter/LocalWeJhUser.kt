package com.zjutjh.ijh.datastore.converter

import com.zjutjh.ijh.datastore.model.LocalWeJhUser
import com.zjutjh.ijh.datastore.model.LocalWeJhUserKt
import com.zjutjh.ijh.datastore.model.localWeJhUser
import com.zjutjh.ijh.model.WeJhUser
import com.zjutjh.ijh.network.model.NetworkWeJhUser
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

fun WeJhUser.asLocalModel(): LocalWeJhUser =
    localWeJhUser {
        uid = this@asLocalModel.uid
        username = this@asLocalModel.username
        sessionToken = this@asLocalModel.sessionToken
        sessionExpiresAt = this@asLocalModel.sessionExpiresAt.toEpochSecond()
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

fun NetworkWeJhUser.asLocalModel(
    sessionToken: String,
    sessionExpiresAt: Long
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