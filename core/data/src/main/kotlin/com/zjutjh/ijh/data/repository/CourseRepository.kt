package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

/**
 * Data layer Interface of [Course] repository
 */
interface CourseRepository {
    val lastSyncTimeStream: Flow<ZonedDateTime?>

    fun getCourses(year: Int, term: Term): Flow<List<Course>>

    suspend fun sync(year: Int, term: Term)
}
