package com.zjutjh.ijh.database.converter

import androidx.room.TypeConverter
import java.time.DayOfWeek

class DayOfWeekConverter {
    @TypeConverter
    fun dayOfWeekToInt(dayOfWeek: DayOfWeek): Int = dayOfWeek.value

    @TypeConverter
    fun intToDayOfWeek(value: Int): DayOfWeek = DayOfWeek.of(value)
}