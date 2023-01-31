package com.zjutjh.ijh.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zjutjh.ijh.model.Term
import java.time.DayOfWeek

@Entity(
    tableName = "schedule",
    foreignKeys = [ForeignKey(
        entity = CourseEntity::class,
        parentColumns = ["id"],
        childColumns = ["courseId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index(value = ["year", "term"])]
)
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val courseId: Long,
    val year: Int,
    val term: Term,
    val sectionStart: Int,
    val sectionEnd: Int,
    val dayOfWeek: DayOfWeek,
    val weekStart: Int,
    val weekEnd: Int?,
    val evenWeek: Boolean?,
)
