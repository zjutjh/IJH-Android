package com.zjutjh.ijh.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.model.WeJhInfo
import com.zjutjh.ijh.model.toTerm
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * WeJH system Info
 */
@JsonClass(generateAdapter = true)
data class NetworkWeJhInfo(
    @Json(name = "is_begin")
    val isBegin: Boolean,
    val term: String,
    val termYear: String,
    val termStartDate: String,
    val time: String,
    val week: Int,
    val schoolBusUrl: String,
)

fun NetworkWeJhInfo.asExternalModel(): WeJhInfo =
    WeJhInfo(
        isBegin = isBegin,
        term = term.toTerm(),
        year = termYear.toInt(),
        termStartDate = LocalDate.parse(termStartDate),
        week = week,
        lastSyncTime = ZonedDateTime.parse(time),
        schoolBusUrl = schoolBusUrl,
    )
