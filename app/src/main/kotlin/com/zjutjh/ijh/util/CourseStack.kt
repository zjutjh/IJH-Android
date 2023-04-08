package com.zjutjh.ijh.util

import androidx.compose.runtime.Stable
import com.zjutjh.ijh.model.Course
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.max
import kotlin.math.min

@Stable
class CourseStack(
    course: Course,
) {
    var courses: PersistentList<Course> = persistentListOf(course)

    var start: Int = course.sectionStart
        private set
    var end: Int = course.sectionEnd
        private set

    override operator fun equals(other: Any?): Boolean {
        return when (other) {
            null -> false
            is CourseStack -> this.start == other.start && this.end == other.end && this.courses == other.courses
            else -> false
        }
    }

    companion object {
        fun stackConflict(courses: List<Course>): ImmutableList<CourseStack> {
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
                    stack.courses = stack.courses.add(course)
                    stack.start = min(stack.start, course.sectionStart)
                    stack.end = max(stack.end, course.sectionEnd)
                }
            }

            return list.toImmutableList()
        }
    }

    override fun toString(): String {
        return "CourseStack(courses=$courses, start=$start, end=$end)"
    }

    override fun hashCode(): Int {
        var result = courses.hashCode()
        result = 31 * result + start
        result = 31 * result + end
        return result
    }
}