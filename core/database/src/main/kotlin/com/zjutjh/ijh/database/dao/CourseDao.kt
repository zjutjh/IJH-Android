package com.zjutjh.ijh.database.dao

import androidx.room.*
import com.zjutjh.ijh.database.model.CourseEntity
import com.zjutjh.ijh.model.Term
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query(
        """
        SELECT * FROM courses
        WHERE id = :courseId
    """
    )
    fun getCourse(courseId: Long): Flow<CourseEntity>

    @Query("SELECT * FROM courses")
    fun getCourses(): Flow<List<CourseEntity>>

    @Query(
        """
        SELECT * FROM courses
        WHERE year = :year AND term = :term
    """
    )
    fun getCourses(year: Int, term: Term): Flow<List<CourseEntity>>

    @Insert
    suspend fun insertCourse(course: CourseEntity): Long

    @Insert
    suspend fun insertCourses(courses: List<CourseEntity>): List<Long>

    @Delete
    suspend fun deleteCourses(courses: List<CourseEntity>)

    @Query(
        """
        DELETE FROM courses
        WHERE year = :year AND term = :term
    """
    )
    suspend fun deleteCourses(year: Int, term: Term)

    @Transaction
    suspend fun deleteAndInsertCourses(delete: List<CourseEntity>, insert: List<CourseEntity>) {
        deleteCourses(delete)
        insertCourses(insert)
    }

}