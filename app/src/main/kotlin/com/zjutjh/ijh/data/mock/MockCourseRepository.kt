package com.zjutjh.ijh.data.mock

import com.zjutjh.ijh.data.Course
import com.zjutjh.ijh.data.CourseRepository
import com.zjutjh.ijh.data.Section
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.DayOfWeek

/**
 * A mock of [CourseRepository] for preview and tests
 */
class MockCourseRepository : CourseRepository {

    override fun fetchCourses(): ImmutableList<Course> = persistentListOf(
        Course(
            "Design pattern in practice",
            "Mr. Info",
            "Software.A.302",
            "8:00-9:40",
            "1-16 Week | Mon (1-2) | 8:00-9:40",
            "4.0",
            Section(1, 2),
            DayOfWeek.MONDAY
        ), Course(
            "Software Engineering and Information Technology",
            "Mr. Hex",
            "Information.B.101",
            "9:55-11:35",
            "1-16 Week | Mon (1-2) | 8:00-9:40",
            "4.0",
            Section(1, 2),
            DayOfWeek.MONDAY
        )
    )

}