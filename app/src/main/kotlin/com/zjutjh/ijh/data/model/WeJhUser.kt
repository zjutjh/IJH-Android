package com.zjutjh.ijh.data.model

import androidx.compose.runtime.Stable

@Stable
data class WeJhUser(
    val uid: Long,
    val username: String,
    val studentId: String,
    val createTime: String,
    val phoneNum: String,
    val userType: Int,
    val bind: Bind,
) {
    data class Bind(
        val lib: Boolean,
        val yxy: Boolean,
        val zf: Boolean,
    )
}