package com.zjutjh.ijh.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.exception.CourseParseException
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.CourseWeek
import java.time.DayOfWeek

/**
 * **Note:** Due to Zf class table is network-only model, so omit the `Network` prefix.
 */
@JsonClass(generateAdapter = true)
data class ZfClassTable(
    val info: Info,
    val lessonsTable: List<LessonsTable>?,
    val practiceLessons: List<PracticeLesson>?,
) {
    @JsonClass(generateAdapter = true)
    data class Info(
        @Json(name = "ClassName")
        val className: String,
        @Json(name = "Name")
        val name: String
    )

    @JsonClass(generateAdapter = true)
    data class LessonsTable(
        val campus: String,
        val className: String,
        val classID: String,
        val credits: String,
        val id: String,
        val lessonHours: String,
        val lessonName: String,
        val lessonPlace: String,
        val placeID: String,
        val sections: String,
        val teacherName: String,
        val type: String,
        val week: String,
        val weekday: String
    )

    @JsonClass(generateAdapter = true)
    data class PracticeLesson(
        val className: String,
        val credits: String,
        val lessonName: String,
        val teacherName: String
    )
}


/**
 * @throws CourseParseException
 */
fun ZfClassTable.LessonsTable.parseWeekString(): CourseWeek {
    val singles: ArrayList<Int> = ArrayList()
    val ranges: ArrayList<CourseWeek.WeekRange> = ArrayList()
    try {
        for (time in week.split(',')) {
            var evenWeek: Boolean? = null
            val clearTime = if (time.endsWith('周')) {
                time.substring(0, time.length - 1)
            } else if (time.endsWith("周(单)")) {
                evenWeek = false
                time.substring(0, time.length - 4)
            } else if (time.endsWith("周(双)")) {
                evenWeek = true
                time.substring(0, time.length - 4)
            } else {
                time
            }
            val section = clearTime.split('-')
            when (section.size) {
                // Single week
                1 -> singles.add(clearTime.toInt())
                2 -> {
                    val start = section[0].toInt()
                    val end = section[1].toInt()
                    ranges.add(
                        CourseWeek.WeekRange(
                            start,
                            end,
                            evenWeek,
                        )
                    )
                }

                else -> throw CourseParseException("Invalid week section format.")
            }
        }
    } catch (e: NumberFormatException) {
        throw CourseParseException("Fail to parse week numbers.")
    }

    return CourseWeek(ranges = ranges, singles = singles)
}


/**
 * @throws CourseParseException
 */
fun ZfClassTable.LessonsTable.asExternalModel(): Course {
    val section = sections.split('-')
    if (section.size != 2) {
        throw CourseParseException("Invalid class section format.")
    }

    return Course(
        id = 0,
        name = lessonName,
        className = className,
        teacherName = teacherName,
        campus = campus,
        place = lessonPlace,
        type = type,
        credits = credits.toFloat(),
        hours = lessonHours.toInt(),
        dayOfWeek = DayOfWeek.of(weekday.toInt()),
        sectionStart = section[0].toInt(),
        sectionEnd = section[1].toInt(),
        weeks = parseWeekString()
    )
}