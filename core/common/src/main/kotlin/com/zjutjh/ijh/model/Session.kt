package com.zjutjh.ijh.model

import java.time.ZonedDateTime

data class Session(
    val token: String,
    val expiresAt: ZonedDateTime,
)