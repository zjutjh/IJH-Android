package com.zjutjh.ijh.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zjutjh.ijh.database.model.CourseEntity
import com.zjutjh.ijh.database.model.ScheduleEntity
import com.zjutjh.ijh.model.Term
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query(
        """
        SELECT * FROM schedule
        WHERE id = :id
    """
    )
    fun getSchedule(id: Long): Flow<ScheduleEntity>

    @Query("SELECT * FROM schedule")
    fun getSchedules(): Flow<List<ScheduleEntity>>

    @Query(
        """
        SELECT * FROM schedule
        WHERE year = :year AND term = :term
    """
    )
    fun getSchedules(year: Int, term: Term): Flow<List<ScheduleEntity>>

    @Insert
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    @Insert
    suspend fun insertSchedules(schedules: List<ScheduleEntity>): List<Long>

//    @Query(
//        """
//        SELECT
//            c.id AS courseId,
//            c.name AS name,
//            c.className AS className,
//            c.teacherName AS teacherName,
//            c.campus AS campus,
//            c.place AS place,
//            c.type AS type,
//            c.credits AS credits,
//            c.hours = hours,
//            s.id AS scheduleId,
//            s.sectionStart AS sectionStart,
//            s.sectionEnd AS sectionEnd,
//            s.dayOfWeek AS dayOfWeek,
//            s.weeks AS weeks
//        FROM courses AS c
//        JOIN schedule AS s ON c.id = s.courseId
//    """
//    )
//    fun getCourseSchedule(): Flow<Course>
//
//    @Query(
//        """
//        SELECT
//            c.id AS courseId,
//            c.name AS name,
//            c.className AS className,
//            c.teacherName AS teacherName,
//            c.campus AS campus,
//            c.place AS place,
//            c.type AS type,
//            c.credits AS credits,
//            c.hours = k.hours,
//            s.id AS scheduleId,
//            s.sectionStart AS sectionStart,
//            s.sectionEnd AS sectionEnd,
//            s.dayOfWeek AS dayOfWeek,
//            s.weeks AS weeks
//        FROM courses AS c
//        JOIN schedule AS s ON c.id = s.courseId
//        WHERE year = :year AND term = :term
//    """
//    )
//    fun getCourseSchedule(year: Int, term: Term): Flow<Course>

    @Query(
        """
        SELECT * FROM courses AS c
        JOIN schedule AS s ON c.id = s.courseId
    """
    )
    fun getCourseScheduleMultimap(): Flow<Map<CourseEntity, List<ScheduleEntity>>>

    @Query(
        """
        SELECT * FROM courses AS c
        JOIN schedule AS s ON c.id = s.courseId
        WHERE year = :year AND term = :term
    """
    )
    fun getCourseScheduleMultimap(
        year: Int,
        term: Term
    ): Flow<Map<CourseEntity, List<ScheduleEntity>>>

    @Query(
        """
        DELETE FROM courses
        WHERE 
        WHERE year = :year AND term = :term
        """
    )
    suspend fun deleteCourseSchedule(year: Int, term: Term)
}