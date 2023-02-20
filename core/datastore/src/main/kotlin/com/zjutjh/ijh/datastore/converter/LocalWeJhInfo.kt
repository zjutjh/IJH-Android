package com.zjutjh.ijh.datastore.converter

import com.zjutjh.ijh.datastore.model.WeJhPreference
import com.zjutjh.ijh.datastore.model.WeJhPreferenceKt
import com.zjutjh.ijh.model.WeJhInfo
import com.zjutjh.ijh.model.toTerm
import java.time.LocalDate

fun WeJhPreference.Info.asExternalModel(): WeJhInfo =
    WeJhInfo(
        term = term.toTerm(),
        year = year,
        termStartDate = LocalDate.ofEpochDay(termStartDate),
        lastSyncTime = lastSyncTime.toZonedDateTime(),
        schoolBusUrl = schoolBusUrl,
    )

fun WeJhInfo.asLocalModel(): WeJhPreference.Info =
    WeJhPreferenceKt.info {
        term = this@asLocalModel.term.ordinal
        year = this@asLocalModel.year
        termStartDate = this@asLocalModel.termStartDate.toEpochDay()
        lastSyncTime = this@asLocalModel.lastSyncTime.toEpochSecond()
        schoolBusUrl = this@asLocalModel.schoolBusUrl
    }
