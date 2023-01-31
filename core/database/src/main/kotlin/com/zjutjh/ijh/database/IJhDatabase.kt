package com.zjutjh.ijh.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zjutjh.ijh.database.converter.CourseWeekConverter
import com.zjutjh.ijh.database.converter.DayOfWeekConverter
import com.zjutjh.ijh.database.converter.TermConverter
import com.zjutjh.ijh.database.dao.CourseDao
import com.zjutjh.ijh.database.model.CourseEntity

@Database(
    version = IJhDatabase.VERSION,
    entities = [CourseEntity::class],
)
@TypeConverters(
    CourseWeekConverter::class,
    TermConverter::class,
    DayOfWeekConverter::class,
)
abstract class IJhDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {
        const val VERSION = 1
    }
}