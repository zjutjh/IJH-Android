package com.zjutjh.ijh.data.model

import com.zjutjh.ijh.datastore.model.WeJhPreferenceKt
import com.zjutjh.ijh.model.toTerm
import com.zjutjh.ijh.network.model.NetworkWeJhInfo
import java.time.LocalDate
import java.time.ZonedDateTime

fun NetworkWeJhInfo.asLocalModel() =
    WeJhPreferenceKt.info {
        this.isBegin = this@asLocalModel.isBegin
        this.term = this@asLocalModel.term.toTerm().ordinal
        this.year = this@asLocalModel.termYear.toInt()
        this.termStartDate = LocalDate.parse(this@asLocalModel.termStartDate).toEpochDay()
        this.week = this@asLocalModel.week
        this.lastSyncTime = ZonedDateTime.parse(this@asLocalModel.time).toEpochSecond()
        this.schoolBusUrl = this@asLocalModel.schoolBusUrl
    }