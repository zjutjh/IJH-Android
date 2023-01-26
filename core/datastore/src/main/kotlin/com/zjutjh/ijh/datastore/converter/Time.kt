package com.zjutjh.ijh.datastore.converter

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun Long.toZonedDateTime(zone: ZoneId = ZoneOffset.UTC): ZonedDateTime =
    ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(this),
        zone
    )