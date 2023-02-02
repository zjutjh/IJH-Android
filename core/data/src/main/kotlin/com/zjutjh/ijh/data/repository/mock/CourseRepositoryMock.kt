package com.zjutjh.ijh.data.repository.mock

import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.CourseWeek
import com.zjutjh.ijh.model.Term
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.DayOfWeek
import java.time.ZonedDateTime

/**
 * A mock of [CourseRepository] for preview and tests
 */
class CourseRepositoryMock : CourseRepository {

    override val lastSyncTimeStream: Flow<ZonedDateTime?> = flowOf(
        ZonedDateTime.now()
    )

    override fun getCourses(year: Int, term: Term): Flow<List<Course>> = flowOf(getCourses())

    override suspend fun sync(year: Int, term: Term) = Unit

    companion object {
        fun getCourse(): Course = Course(
            id = 1,
            name = "Design pattern in practice",
            teacherName = "Mr. Info",
            place = "Software.A.302",
            campus = "PF",
            type = "A",
            credits = 4.0f,
            hours = 64,
            className = "001",
            sectionStart = 3,
            sectionEnd = 4,
            dayOfWeek = DayOfWeek.MONDAY,
            weeks = CourseWeek(
                listOf(),
                listOf(
                    CourseWeek.WeekRange(1, 16, null)
                )
            )
        )

        fun getCourses(): List<Course> = listOf(
            Course(
                id = 1,
                name = "Design pattern in practice",
                teacherName = "Mr. Info",
                place = "Software.A.302",
                campus = "PF",
                type = "A",
                credits = 4.0f,
                hours = 64,
                className = "001",
                sectionStart = 1,
                sectionEnd = 2,
                dayOfWeek = DayOfWeek.MONDAY,
                weeks = CourseWeek(
                    listOf(),
                    listOf(
                        CourseWeek.WeekRange(1, 16, null)
                    )
                )
            ),
            Course(
                id = 2,
                name = "Software Engineering and Information Technology",
                teacherName = "Mr. Hex",
                place = "Information.B.101",
                campus = "PF",
                type = "B",
                credits = 4.0f,
                hours = 64,
                className = "002",
                sectionStart = 3,
                sectionEnd = 4,
                dayOfWeek = DayOfWeek.MONDAY,
                weeks = CourseWeek(
                    listOf(),
                    listOf(
                        CourseWeek.WeekRange(1, 16, null)
                    )
                )
            ),
            Course(
                id = 3,
                name = "Design pattern in practice",
                teacherName = "Mr. Info",
                place = "Software.A.302",
                campus = "PF",
                type = "A",
                credits = 4.0f,
                hours = 64,
                className = "001",
                sectionStart = 6,
                sectionEnd = 8,
                dayOfWeek = DayOfWeek.MONDAY,
                weeks = CourseWeek(
                    listOf(),
                    listOf(
                        CourseWeek.WeekRange(1, 16, null)
                    )
                )
            ),
        )
    }
}