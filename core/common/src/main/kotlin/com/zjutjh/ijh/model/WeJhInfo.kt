package com.zjutjh.ijh.model

import androidx.compose.runtime.Stable
import java.time.LocalDate
import java.time.ZonedDateTime

@Stable
data class WeJhInfo(
    val isBegin: Boolean,
    val term: Term,
    val year: Int,
    val termStartDate: LocalDate,
    val week: Int,
    val lastSyncTime: ZonedDateTime,
    val schoolBusUrl: String,
)