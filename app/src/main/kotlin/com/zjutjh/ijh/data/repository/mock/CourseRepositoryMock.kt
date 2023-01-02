package com.zjutjh.ijh.data.repository.mock

import com.zjutjh.ijh.data.model.Course
import com.zjutjh.ijh.data.repository.CourseRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * A mock of [CourseRepository] for preview and tests
 */
class CourseRepositoryMock : CourseRepository {

    override suspend fun getCourses(): ImmutableList<Course> = Companion.getCourses()

    companion object {
        fun getCourse(): Course = Course(
            name = "Design pattern in practice",
            teacher = "Mr. Info",
            place = "Software.A.302",
            campus = "PF",
            credit = "4.0",
            className = "001",
            sectionStart = 3,
            sectionEnd = 4,
            dayOfWeek = 0,
            weekStart = 1,
            weekEnd = 16,
        )

        fun getCourses(): ImmutableList<Course> = persistentListOf(
            Course(
                name = "Design pattern in practice",
                teacher = "Mr. Info",
                place = "Software.A.302",
                campus = "PF",
                credit = "4.0",
                className = "001",
                sectionStart = 1,
                sectionEnd = 2,
                dayOfWeek = 0,
                weekStart = 1,
                weekEnd = 16,
            ), Course(
                name = "Software Engineering and Information Technology",
                teacher = "Mr. Hex",
                place = "Information.B.101",
                campus = "PF",
                credit = "4.0",
                className = "002",
                sectionStart = 3,
                sectionEnd = 4,
                dayOfWeek = 0,
                weekStart = 1,
                weekEnd = 8,
            )
        )
    }
}