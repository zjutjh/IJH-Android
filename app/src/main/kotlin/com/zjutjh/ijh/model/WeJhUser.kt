package com.zjutjh.ijh.model

import androidx.compose.runtime.Stable
import java.time.ZonedDateTime

@Stable
data class WeJhUser(
    val uid: Long,
    val username: String,
    val sessionToken: String,
    val sessionExpiresAt: ZonedDateTime,
    val studentId: String,
    val createTime: ZonedDateTime,
    val phoneNumber: String,
    val userType: Int,
    val bind: Bind,
) {
    data class Bind(
        val lib: Boolean,
        val yxy: Boolean,
        val zf: Boolean,
    )

    fun isEmpty(): Boolean = uid == 0L
}