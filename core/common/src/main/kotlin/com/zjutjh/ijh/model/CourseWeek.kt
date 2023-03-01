package com.zjutjh.ijh.model

import com.zjutjh.ijh.exception.CourseParseException
import java.text.ParseException

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
                throw CourseParseException("Fail to parse week numbers.")
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