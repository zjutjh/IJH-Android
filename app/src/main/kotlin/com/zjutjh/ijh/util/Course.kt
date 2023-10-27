package com.zjutjh.ijh.util

import androidx.core.os.LocaleListCompat
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Section
import java.time.format.DateTimeFormatter

fun Course.shortTime(): String {
    val (start, _) = Section.PAIRS[sectionStart - 1]
    val (_, end) = Section.PAIRS[sectionEnd - 1]

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return "${start.format(formatter)} - ${end.format(formatter)} | $sectionStart-$sectionEnd"
}

fun Course.detailedTime(): String {
    val (start, _) = Section.PAIRS[sectionStart - 1]
    val (_, end) = Section.PAIRS[sectionEnd - 1]

    val startTime = start.format(Section.TIME_FORMATTER)
    val endTime = end.format(Section.TIME_FORMATTER)

    val locale = LocaleListCompat.getDefault()[0]

    val dayOfWeek = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, locale)
    return "${dayOfWeek}($sectionStart-$sectionEnd) | $startTime-$endTime"
}
