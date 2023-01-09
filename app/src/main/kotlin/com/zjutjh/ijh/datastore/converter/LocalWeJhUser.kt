package com.zjutjh.ijh.datastore.converter

import com.zjutjh.ijh.datastore.model.LocalWeJhUser
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