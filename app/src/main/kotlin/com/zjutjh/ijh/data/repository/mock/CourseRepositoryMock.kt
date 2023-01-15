package com.zjutjh.ijh.data.repository.mock

import com.zjutjh.ijh.data.model.Course
import com.zjutjh.ijh.data.repository.CourseRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.DayOfWeek

/**
 * A mock of [CourseRepository] for preview and tests
 */
class CourseRepositoryMock : CourseRepository {

    override suspend fun getCourses(): ImmutableList<Course> = Companion.getCourses()

    companion object {
        fun getCourse(): Course = Course(
            id = 1,
            name = "Design pattern in practice",
            teacherName = "Mr. Info",
            place = "Software.A.302",
            campus = "PF",
            type = "A",
            credit = 4.0f,
            hours = 64,
            className = "001",
            sectionStart = 3,
            sectionEnd = 4,
            dayOfWeek = DayOfWeek.MONDAY,
            weekStart = 1,
            weekEnd = 16,
        )

        fun getCourses(): ImmutableList<Course> = persistentListOf(
            Course(
                id = 1,
                name = "Design pattern in practice",
                teacherName = "Mr. Info",
                place = "Software.A.302",
                campus = "PF",
                type = "A",
                credit = 4.0f,
                hours = 64,
                className = "001",
                sectionStart = 1,
                sectionEnd = 2,
                dayOfWeek = DayOfWeek.MONDAY,
                weekStart = 1,
                weekEnd = 16,
            ), Course(
                id = 2,
                name = "Software Engineering and Information Technology",
                teacherName = "Mr. Hex",
                place = "Information.B.101",
                campus = "PF",
                type = "B",
                credit = 4.0f,
                hours = 64,
                className = "002",
                sectionStart = 3,
                sectionEnd = 4,
                dayOfWeek = DayOfWeek.MONDAY,
                weekStart = 1,
                weekEnd = 8,
            )
        )
    }
}