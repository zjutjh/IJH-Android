package com.zjutjh.ijh.model

import androidx.compose.runtime.Immutable
import java.time.DayOfWeek

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

data class Section(val left: Int, val right: Int)