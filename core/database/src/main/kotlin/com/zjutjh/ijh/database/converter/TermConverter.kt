package com.zjutjh.ijh.database.converter

import androidx.room.TypeConverter
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.model.toTerm

class TermConverter {
    @TypeConverter
    fun termToInt(term: Term): Int = term.ordinal

    @TypeConverter
    fun intToTerm(ordinal: Int): Term = ordinal.toTerm()
}