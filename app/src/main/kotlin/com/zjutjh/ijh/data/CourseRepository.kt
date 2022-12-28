package com.zjutjh.ijh.data

import kotlinx.collections.immutable.ImmutableList

/**
 * Data layer Interface of [Course] repository
 */
interface CourseRepository {

    fun getCourses(): ImmutableList<Course>

}


