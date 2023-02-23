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

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + term.hashCode()
        result = 31 * result + week
        result = 31 * result + isInTerm.hashCode()
        return result
    }
}
