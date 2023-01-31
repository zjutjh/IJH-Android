package com.zjutjh.ijh.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.CourseWeek
import com.zjutjh.ijh.model.Term
import java.time.DayOfWeek

@Entity(
    tableName = "courses",
    indices = [Index(value = ["year", "term"])]
)
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val teacherName: String,
    val campus: String,
    val place: String,
    val className: String,
    val type: String,
    val credits: Float,
    val hours: Int,
    val year: Int,
    val term: Term,
    val sectionStart: Int,
    val sectionEnd: Int,
    val dayOfWeek: DayOfWeek,
    val weeks: CourseWeek
) {

    fun asExternalModel(): Course =
        Course(
            id,
            name,
            teacherName,
            campus,
            place,
            className,
            type,
            credits,
            hours,
            sectionStart,
            sectionEnd,
            dayOfWeek,
            weeks
        )
}