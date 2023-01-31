package com.zjutjh.ijh.database.converter

import androidx.room.TypeConverter
import com.zjutjh.ijh.model.CourseWeek

class CourseWeekConverter {
    @TypeConverter
    fun courseWeekToString(courseWeek: CourseWeek): String = courseWeek.serialize()

    @TypeConverter
    fun stringToCourseWeek(serialized: String): CourseWeek =
        CourseWeek.Serializer.deserialize(serialized)
}