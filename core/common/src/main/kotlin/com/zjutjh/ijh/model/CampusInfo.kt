package com.zjutjh.ijh.model

import androidx.compose.runtime.Stable
import java.time.LocalDate
import java.time.ZonedDateTime

@Stable
data class CampusInfo(
    val term: Term,
    val year: Int,
    val termStartDate: LocalDate,
    val lastSyncTime: ZonedDateTime,
    val schoolBusUrl: String,
)