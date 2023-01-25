package com.zjutjh.ijh.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zjutjh.ijh.exception.CourseParseException
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.CourseWeek
import java.time.DayOfWeek

@JsonClass(generateAdapter = true)
data class ClassTable(
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
fun ClassTable.LessonsTable.asExternalModel(): Course {
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
        weeks = CourseWeek.parseFromWeekString(week)
    )
}
