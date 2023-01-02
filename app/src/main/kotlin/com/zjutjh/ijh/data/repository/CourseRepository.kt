package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.data.model.Course
import kotlinx.collections.immutable.ImmutableList

/**
 * Data layer Interface of [Course] repository
 */
interface CourseRepository {
    suspend fun getCourses(): ImmutableList<Course>
}


