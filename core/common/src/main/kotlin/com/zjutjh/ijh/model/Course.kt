package com.zjutjh.ijh.model

import androidx.compose.runtime.Stable
import java.time.DayOfWeek
import java.time.LocalTime

@Stable
data class Course(
    val id: Long,
    val name: String,
    val teacherName: String,
    val campus: String,
    val place: String,
    val className: String,
    val type: String,
    val credits: Float,
    val hours: Int,
    val sectionStart: Int,
    val sectionEnd: Int,
    val dayOfWeek: DayOfWeek,
    val weeks: CourseWeek,
) {
    fun coloringHashCode(): UInt {
        var result = name.hashCode()
        result = 31 * result + teacherName.hashCode()
        result = 31 * result + campus.hashCode()
        result = 31 * result + className.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + credits.hashCode()
        result = 31 * result + hours
        return result.toUInt()
    }

    companion object {
        fun currentSection(time: LocalTime = LocalTime.now()): Pair<Int, Float> {
            for (i in Section.PAIRS.indices) {
                val (start, end) = Section.PAIRS[i]
                if (time < start) {
                    // Class is not started
                    return Pair(i, -1f)
                } else if (time <= end) {
                    val current = time.toSecondOfDay() - start.toSecondOfDay()
                    return Pair(i, current.toFloat() / (Section.DURATION))
                }
            }
            // Not found
            return Pair(-1, -1f)
        }

        fun default(): Course =
            Course(
                id = 0,
                name = String(),
                teacherName = String(),
                campus = String(),
                place = String(),
                className = String(),
                type = String(),
                credits = 0f,
                hours = 0,
                sectionStart = 0,
                sectionEnd = 0,
                dayOfWeek = DayOfWeek.MONDAY,
                weeks = CourseWeek.default(),
            )
    }
}