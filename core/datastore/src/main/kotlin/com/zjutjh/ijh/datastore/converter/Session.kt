package com.zjutjh.ijh.datastore.converter

import com.zjutjh.ijh.datastore.model.LocalSession
import com.zjutjh.ijh.datastore.model.localSession
import com.zjutjh.ijh.model.Session
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun LocalSession.asExternalModel(): Session =
    Session(this.token, ZonedDateTime.ofInstant(Instant.ofEpochSecond(this.expirationTime),ZoneOffset.UTC))

fun Session.asLocalModel(): LocalSession =
    localSession {
        this.token = this@asLocalModel.token
        this.expirationTime = this@asLocalModel.expiresAt.toEpochSecond()
    }