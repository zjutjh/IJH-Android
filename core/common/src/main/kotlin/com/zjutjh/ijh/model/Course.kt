package com.zjutjh.ijh.model

import androidx.compose.runtime.Stable
import com.zjutjh.ijh.exception.CourseParseException
import java.text.ParseException
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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

    fun equalsIgnoreId(other: Course): Boolean =
        name == other.name &&
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

        val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")
        private const val COURSE_DURATION: Int = 45 * 60

        private fun hmToLocalTimePair(
            hour1: Int,
            minute1: Int,
            hour2: Int,
            minute2: Int
        ): Pair<LocalTime, LocalTime> =
            Pair(LocalTime.of(hour1, minute1), LocalTime.of(hour2, minute2))

        fun currentSection(time: LocalTime = LocalTime.now()): Pair<Int, Float> {
            for (i in SECTIONS.indices) {
                val (start, end) = SECTIONS[i]
                if (time < start) {
                    // Class is not started
                    return Pair(i, -1f)
                } else if (time <= end) {
                    val current = time.toSecondOfDay() - start.toSecondOfDay()
                    return Pair(i, current.toFloat() / (COURSE_DURATION))
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

data class CourseWeek(
    /**
     * Single course weeks
     */
    val singles: List<Int>,
    val ranges: List<WeekRange>,
) {
    data class WeekRange(
        val start: Int,
        val end: Int,
        /**
         * `true` for even week, `false` for odd week, `null` for invalid.
         */
        val oddOrEvenWeek: Boolean? = null,
    ) {
        override fun toString(): String {
            return if (oddOrEvenWeek == null) {
                "$start-$end"
            } else if (oddOrEvenWeek) {
                "$start-${end}双"
            } else {
                "$start-${end}单"
            }
        }
    }

    /**
     * Checks if the specified week is contained in weeks
     */
    operator fun contains(week: Int): Boolean {
        if (singles.contains(week))
            return true
        for (i in ranges) {
            if (week in i.start..i.end) {
                val rem = week % 2
                when {
                    i.oddOrEvenWeek == null -> return true
                    i.oddOrEvenWeek && (rem == 0) -> return true
                    !i.oddOrEvenWeek && (rem == 1) -> return true
                }
            }
        }
        return false
    }

    override fun toString(): String =
        buildString {
            singles.forEachIndexed { index, i ->
                if (index != 0) {
                    append(", ")
                }
                append(i)
            }
            if (singles.isNotEmpty() && ranges.isNotEmpty())
                append(", ")
            ranges.forEach {
                append(it.toString())
            }
        }


    /**
     * Serialize the object into a readable string.
     */
    fun serialize(): String = Serializer.serialize(this)

    companion object {

        /**
         * @throws CourseParseException
         */
        fun parseFromZfWeekString(week: String): CourseWeek {
            val singles: ArrayList<Int> = ArrayList()
            val ranges: ArrayList<WeekRange> = ArrayList()
            try {
                for (time in week.split(',')) {
                    val clearTime = time.removeSuffix("周") // Remove useless suffix
                    val section = clearTime.split('-')
                    when (section.size) {
                        // Single week
                        1 -> singles.add(clearTime.toInt())
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
                            ranges.add(
                                WeekRange(
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

            return CourseWeek(ranges = ranges, singles = singles)
        }

        fun default(): CourseWeek =
            CourseWeek(
                emptyList(),
                emptyList(),
            )
    }

    object Serializer {

        /**
         * Serialize the object into a readable string.
         *
         * - `S1`: Single week,
         * - `R1-16`: Week range,
         * - `O1-16`: Odd week in week range,
         * - `E1-16`: Even week in week range,
         */
        fun serialize(obj: CourseWeek): String =
            buildString {
                for (i in obj.singles) {
                    append('S').append(i)
                }
                for (i in obj.ranges) {
                    if (i.oddOrEvenWeek == null) {
                        append('R')
                    } else if (i.oddOrEvenWeek) {
                        append('E')
                    } else {
                        append('O')
                    }
                    append(i.start).append('-').append(i.end)
                }
            }

        private enum class State {
            START,
            S,
            R1,
            R2,
        }

        /**
         * @throws ParseException
         */
        fun deserialize(input: String): CourseWeek {
            val singles: ArrayList<Int> = ArrayList()
            val ranges: ArrayList<WeekRange> = ArrayList()

            var state: State = State.START
            var flag: Boolean? = null
            var v1 = 0
            var v2 = 0

            fun setS() {
                state = State.S
                v1 = 0
            }

            fun setR() {
                state = State.R1
                flag = null
                v1 = 0
                v2 = 0
            }

            fun setO() {
                state = State.R1
                flag = false
                v1 = 0
                v2 = 0
            }

            fun setE() {
                state = State.R1
                flag = true
                v1 = 0
                v2 = 0
            }

            fun postS() {
                if (v1 != 0) {
                    singles.add(v1)
                }
            }

            fun postR() {
                if (v1 != 0 && v2 != 0) {
                    ranges.add(
                        WeekRange(start = v1, end = v2, oddOrEvenWeek = flag)
                    )
                }
            }


            for ((index: Int, value: Char) in input.withIndex()) {
                when (state) {
                    State.START -> when (value) {
                        'S' -> setS()
                        'R' -> setR()
                        'O' -> setO()
                        'E' -> setE()
                        else -> throw ParseException("Unexpected character get.", index)
                    }
                    State.S -> when (value) {
                        in '0'..'9' -> {
                            v1 = v1 * 10 + value.digitToInt()
                        }
                        'S' -> {
                            postS()
                            setS()
                        }
                        'R' -> {
                            postS()
                            setR()
                        }
                        'O' -> {
                            postS()
                            setO()
                        }
                        'E' -> {
                            postS()
                            setE()
                        }
                        else -> throw ParseException("Unexpected character get.", index)
                    }
                    State.R1 -> when (value) {
                        in '0'..'9' -> {
                            v1 = v1 * 10 + value.digitToInt()
                        }
                        '-' -> state = State.R2
                        else -> throw ParseException("Unexpected character get.", index)
                    }
                    State.R2 -> when (value) {
                        in '0'..'9' -> {
                            v2 = v2 * 10 + value.digitToInt()
                        }
                        'S' -> {
                            postR()
                            setS()
                        }
                        'R' -> {
                            postR()
                            setR()
                        }
                        'O' -> {
                            postR()
                            setO()
                        }
                        'E' -> {
                            postR()
                            setE()
                        }
                        else -> throw ParseException("Unexpected character get.", index)
                    }
                }
            }

            // End of flow/string.
            when (state) {
                State.START -> Unit
                State.S -> postS()
                State.R2 -> postR()
                else -> throw ParseException("Bad end state.", input.length)
            }

            return CourseWeek(singles = singles, ranges = ranges)
        }
    }
}