package com.zjutjh.ijh.data

import androidx.compose.runtime.Immutable
import java.time.DayOfWeek

data class Section(val left: Int, val right: Int)

@Immutable
data class Course(
    val name: String,
    val teacher: String,
    val place: String,
    val shortTime: String,
    val longTime: String,
    val credit: String,
    val section: Section,
    val dayOfWeek: DayOfWeek,
)
