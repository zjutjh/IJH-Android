package com.zjutjh.ijh.datastore.converter

import com.zjutjh.ijh.datastore.model.IJhPreference
import com.zjutjh.ijh.datastore.model.IJhPreferenceKt
import com.zjutjh.ijh.model.CampusInfo
import com.zjutjh.ijh.model.toTerm
import java.time.LocalDate

fun IJhPreference.Campus.asExternalModel(): CampusInfo =
    CampusInfo(
        term = term.toTerm(),
        year = year,
        termStartDate = LocalDate.ofEpochDay(termStartDate),
        lastSyncTime = syncTime.toZonedDateTime(),
        schoolBusUrl = schoolBusUrl,
    )

fun CampusInfo.asLocalModel(): IJhPreference.Campus =
    IJhPreferenceKt.campus {
        term = this@asLocalModel.term.ordinal
        year = this@asLocalModel.year
        termStartDate = this@asLocalModel.termStartDate.toEpochDay()
        syncTime = this@asLocalModel.lastSyncTime.toEpochSecond()
        schoolBusUrl = this@asLocalModel.schoolBusUrl
    }
