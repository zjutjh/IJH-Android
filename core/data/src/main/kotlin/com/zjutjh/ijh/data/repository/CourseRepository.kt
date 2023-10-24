package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.ZonedDateTime

/**
 * Data layer Interface of [Course] repository
 */
interface CourseRepository {
    val lastSyncTimeStream: Flow<ZonedDateTime?>

    /**
     * Get courses of [year] and [term]
     */
    fun getCourses(year: Int, term: Term): Flow<List<Course>>

    /**
     * Get courses of [year] and [term] in [week] and [dayOfWeek]
     */
    fun getCourses(year: Int, term: Term, week: Int, dayOfWeek: DayOfWeek): Flow<List<Course>>

    suspend fun sync(year: Int, term: Term)
}
