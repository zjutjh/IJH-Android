package com.zjutjh.ijh.model

import androidx.compose.runtime.Stable
import com.zjutjh.ijh.exception.CourseParseException
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
    companion object {
        val SECTIONS = arrayOf(
            hmToLocalTimePair(8, 0, 8, 45),
            hmToLocalTimePair(8, 55, 9, 40),
            hmToLocalTimePair(9, 55, 10, 40),
            hmToLocalTimePair(10, 50, 11, 35),
            hmToLocalTimePair(11, 45, 12, 30),
            hmToLocalTimePair(13, 30, 14, 15),
            hmToLocalTimePair(14, 25, 15, 10),
            hmToLocalTimePair(15, 25, 16, 10),
            hmToLocalTimePair(16, 20, 17, 5),
            hmToLocalTimePair(18, 30, 19, 15),
            hmToLocalTimePair(19, 25, 20, 10),
            hmToLocalTimePair(20, 20, 21, 5),
        )

        private fun hmToLocalTimePair(
            hour1: Int,
            minute1: Int,
            hour2: Int,
            minute2: Int
        ): Pair<LocalTime, LocalTime> =
            Pair(LocalTime.of(hour1, minute1), LocalTime.of(hour2, minute2))
    }
}

data class CourseWeek(
    val weekSections: List<WeekSection>,
    /**
     * Single course weeks
     */
    val weeks: List<Int>,
) {
    data class WeekSection(
        val start: Int,
        val end: Int,
        /**
         * `true` for even week, `false` for odd week, `null` for invalid.
         */
        val oddOrEvenWeek: Boolean?,
    )

    companion object {

        /**
         * @throws CourseParseException
         */
        fun parseFromWeekString(week: String): CourseWeek {
            val single: ArrayList<Int> = ArrayList()
            val sections: ArrayList<WeekSection> = ArrayList()
            try {
                for (time in week.split(',')) {
                    val clearTime = time.removeSuffix("周") // Remove useless suffix
                    val section = clearTime.split('-')
                    when (section.size) {
                        // Single week
                        1 -> single.add(clearTime.toInt())
                        2 -> {
                            val start = section[0].toInt()
                            val section1 = section[1]
                            var evenWeek: Boolean? = null
                            val end: Int = if (section1.endsWith('单')) {
                                evenWeek = false
                                section1.substring(0, section1.length - 1).toInt()
                            } else if (section1.endsWith('双')) {
                                evenWeek = true
                                section1.substring(0, section1.length - 1).toInt()
                            } else {
                                section1.toInt()
                            }
                            sections.add(
                                WeekSection(
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
                throw CourseParseException("Fail to week numbers.")
            }

            return CourseWeek(weekSections = sections, weeks = single)
        }
    }
}