package com.zjutjh.ijh.util

import androidx.compose.runtime.Stable
import com.zjutjh.ijh.model.Course
import kotlin.math.max
import kotlin.math.min

@Stable
class CourseStack(
    course: Course,
) {
    private val _courses: MutableList<Course> = mutableListOf(course)
    val courses: List<Course> = _courses

    var start: Int = course.sectionStart
        private set
    var end: Int = course.sectionEnd
        private set

    companion object {
        fun stackConflict(courses: List<Course>): List<CourseStack> {
            val list = mutableListOf<CourseStack>()

            // Store conflict courses in a stack
            courses.forEach { course ->
                val stack = list.find { stack ->
                    // Find conflicted stack
                    stack.start <= course.sectionStart && stack.end >= course.sectionStart ||
                            stack.start <= course.sectionEnd && stack.end >= course.sectionEnd
                }
                if (stack == null) {
                    // New stack
                    list.add(CourseStack(course))
                } else {
                    stack._courses.add(course)
                    stack.start = min(stack.start, course.sectionStart)
                    stack.end = max(stack.end, course.sectionEnd)
                }
            }

            return list
        }
    }
}