package com.zjutjh.ijh.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.model.CampusInfo
import com.zjutjh.ijh.model.toTerm
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * WeJH campus info
 */
@JsonClass(generateAdapter = true)
data class NetworkCampusInfo(
    @Json(name = "is_begin")
    val isBegin: Boolean,
    val term: String,
    val termYear: String,
    val termStartDate: String,
    val time: String,
    val week: Int,
    val schoolBusUrl: String,
)

fun NetworkCampusInfo.asExternalModel(): CampusInfo =
    CampusInfo(
        term = term.toTerm(),
        year = termYear.toInt(),
        termStartDate = LocalDate.parse(termStartDate),
        lastSyncTime = ZonedDateTime.parse(time),
        schoolBusUrl = schoolBusUrl,
    )
