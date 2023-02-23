package com.zjutjh.ijh.util

import com.zjutjh.ijh.model.Course

fun List<Course>.stackConflict(): List<Triple<List<Course>, Int, Int>> {
    val list = mutableListOf<Triple<List<Course>, Int, Int>>()

    // Store conflict courses in a stack
    var stack = mutableListOf<Course>()
    var start = 0
    var end = 0
    this.forEach { course ->
        val before = stack.lastOrNull()
        if (before == null) {
            start = course.sectionStart
            end = course.sectionEnd
            stack.add(course)
        } else if (before.sectionEnd > course.sectionStart) {
            // Conflict encountered
            end = course.sectionEnd
            stack.add(course)
        } else {
            list.add(Triple(stack, start, end))
            // New stack
            start = course.sectionStart
            end = course.sectionEnd
            stack = mutableListOf(course)
        }
    }

    // End of list
    if (stack.isNotEmpty()) {
        list.add(Triple(stack, start, end))
    }

    return list
}