package com.zjutjh.ijh.ui.model

import com.zjutjh.ijh.model.Term

open class TermWeekState(
    val year: Int,
    val term: Term,
    val week: Int,
    val isInTerm: Boolean,
) {
    override operator fun equals(other: Any?): Boolean {
        return when (other) {
            null -> false
            is TermWeekState -> {
                year == other.year && term == other.term && week == other.week
            }
            else -> false
        }
    }

    fun equalsIgnoreWeek(other: TermWeekState?): Boolean {
        return when (other) {
            null -> false
            else -> {
                year == other.year && term == other.term
            }
        }
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + term.hashCode()
        result = 31 * result + week
        result = 31 * result + isInTerm.hashCode()
        return result
    }

    fun previousWeek(): TermWeekState {
        return if (week == WEEK_START) {
            throw IllegalStateException("Week cannot be less than $WEEK_START")
        } else {
            TermWeekState(year, term, week - 1, isInTerm)
        }
    }

    fun nextWeek(): TermWeekState {
        return if (week == WEEK_END) {
            throw IllegalStateException("Week cannot be greater than $WEEK_END")
        } else {
            TermWeekState(year, term, week + 1, isInTerm)
        }
    }

    fun hasPreviousWeek(): Boolean {
        return week > WEEK_START
    }

    fun hasNextWeek(): Boolean {
        return week < WEEK_END
    }

    companion object {
        const val WEEK_START = 1
        const val WEEK_END = 20
    }
}
