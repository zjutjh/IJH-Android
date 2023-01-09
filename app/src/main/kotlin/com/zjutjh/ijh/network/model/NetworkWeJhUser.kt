package com.zjutjh.ijh.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.model.WeJhUser
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class NetworkWeJhUser(
    val id: Long,
    val username: String,
    @Transient
    var sessionToken: String? = null,
    @Transient
    var sessionExpiresAt: ZonedDateTime? = null,
    val studentID: String,
    val createTime: String,
    @Json(name = "phoneNum")
    val phoneNumber: String,
    val userType: Int,
    val bind: Bind,
) {
    @JsonClass(generateAdapter = true)
    data class Bind(
        val zf: Boolean,
        val lib: Boolean,
        val yxy: Boolean,
    )
}

fun NetworkWeJhUser.asExternalModel() = WeJhUser(
    uid = id,
    username = username,
    sessionToken = sessionToken!!,
    sessionExpiresAt = sessionExpiresAt!!,
    studentId = studentID,
    phoneNumber = phoneNumber,
    createTime = ZonedDateTime.parse(createTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
    userType = userType,
    bind = bind.asExternalModel(),
)

fun NetworkWeJhUser.Bind.asExternalModel() = WeJhUser.Bind(
    this.lib,
    this.yxy,
    this.zf,
)