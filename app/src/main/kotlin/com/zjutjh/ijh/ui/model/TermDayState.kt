package com.zjutjh.ijh.ui.model

import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.model.WeJhInfo
import java.time.DayOfWeek
import java.time.LocalDate

data class TermDayState(
    val year: Int,
    val term: Term,
    val week: Int,
    val dayOfWeek: DayOfWeek,
    val isInTerm: Boolean,
)

fun WeJhInfo.toTermDayState(): TermDayState {
    val date = LocalDate.now()
    val duration = date.toEpochDay() - termStartDate.toEpochDay()
    val week: Long = duration / 7L + 1L

    return TermDayState(
        year = year,
        term = term,
        week = week.toInt(),
        dayOfWeek = date.dayOfWeek,
        isInTerm = duration > 0 && week in 1..20,
    )
}