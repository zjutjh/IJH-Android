package com.zjutjh.ijh.datastore.converter

import com.zjutjh.ijh.datastore.model.WeJhPreference
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