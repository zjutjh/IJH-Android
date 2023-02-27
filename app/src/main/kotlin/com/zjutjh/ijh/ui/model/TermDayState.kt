package com.zjutjh.ijh.ui.model

import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.model.WeJhInfo
import java.time.DayOfWeek
import java.time.LocalDate

class TermDayState(
    year: Int,
    term: Term,
    week: Int,
    isInTerm: Boolean,
    val dayOfWeek: DayOfWeek,
) : TermWeekState(year, term, week, isInTerm) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            null -> false
            is TermDayState -> {
                super.equals(other) && dayOfWeek == other.dayOfWeek
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + dayOfWeek.hashCode()
        return result
    }
}

fun WeJhInfo.toTermDayState(): TermDayState {
    val date = LocalDate.now()
    val duration = date.toEpochDay() - termStartDate.toEpochDay()
    val week: Long = duration / 7L + 1L

    return TermDayState(
        year = year,
        term = term,
        week = week.toInt(),
        dayOfWeek = date.dayOfWeek,
        isInTerm = duration >= 0 && week in TermWeekState.WEEK_START..TermWeekState.WEEK_END,
    )
}