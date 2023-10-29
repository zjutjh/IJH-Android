package com.zjutjh.ijh.data.converter

import com.zjutjh.ijh.database.model.CourseEntity
import com.zjutjh.ijh.exception.CourseParseException
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.network.model.ZfClassTable
import com.zjutjh.ijh.network.model.parseWeekString
import java.time.DayOfWeek

fun ZfClassTable.LessonsTable.asLocalModel(year: Int, term: Term): CourseEntity {
    val section = sections.split('-')
    if (section.size != 2) {
        throw CourseParseException("Invalid class section format.")
    }

    return CourseEntity(
        id = 0,
        name = lessonName,
        className = className,
        teacherName = teacherName,
        campus = campus,
        place = lessonPlace,
        type = type,
        credits = credits.toFloat(),
        year = year,
        term = term,
        hours = lessonHours.toInt(),
        dayOfWeek = DayOfWeek.of(weekday.toInt()),
        sectionStart = section[0].toInt(),
        sectionEnd = section[1].toInt(),
        weeks = parseWeekString()
    )
}

fun CourseEntity.equalsIgnoreId(other: CourseEntity): Boolean {
    return name == other.name &&
            teacherName == other.teacherName &&
            campus == other.campus &&
            place == other.place &&
            className == other.className &&
            type == other.type &&
            credits == other.credits &&
            hours == other.hours &&
            sectionStart == other.sectionStart &&
            sectionEnd == other.sectionEnd &&
            dayOfWeek == other.dayOfWeek &&
            weeks == other.weeks
}