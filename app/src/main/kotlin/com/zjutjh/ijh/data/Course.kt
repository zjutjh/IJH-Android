package com.zjutjh.ijh.data

import androidx.compose.runtime.Immutable
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.time.LocalTime

@Entity
@Immutable
data class Course(
    @Id var id: Long = 0,
    var name: String = String(),
    var teacher: String = String(),
    var campus: String = String(),
    var place: String = String(),
    var className: String = String(),
    var credit: String = String(),
    var sectionStart: Int = 1,
    var sectionEnd: Int = 2,
    var dayOfWeek: Int = 0,
    var weekStart: Int = 1,
    var weekEnd: Int = 16,
) {
    companion object {
        val SECTIONS = arrayOf(
            hmToLocalTimePair(8, 0, 8, 45),
            hmToLocalTimePair(8, 55, 9, 40),
            hmToLocalTimePair(9, 55, 10, 40),
            hmToLocalTimePair(10, 50, 11, 35),
            hmToLocalTimePair(11, 45, 12, 30),
            hmToLocalTimePair(13, 30, 14, 15),
            hmToLocalTimePair(14, 25, 15, 10),
            hmToLocalTimePair(15, 25, 16, 10),
            hmToLocalTimePair(16, 20, 17, 5),
            hmToLocalTimePair(18, 30, 19, 15),
            hmToLocalTimePair(19, 25, 20, 10),
            hmToLocalTimePair(20, 20, 21, 5),
        )

        private fun hmToLocalTimePair(
            hour1: Int,
            minute1: Int,
            hour2: Int,
            minute2: Int
        ): Pair<LocalTime, LocalTime> =
            Pair(LocalTime.of(hour1, minute1), LocalTime.of(hour2, minute2))
    }
}
