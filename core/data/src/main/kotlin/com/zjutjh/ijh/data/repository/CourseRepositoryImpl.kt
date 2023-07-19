package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.data.model.asLocalModel
import com.zjutjh.ijh.data.model.equalsIgnoreId
import com.zjutjh.ijh.database.dao.CourseDao
import com.zjutjh.ijh.database.model.CourseEntity
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.toZonedDateTime
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.network.ZfDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default impl of [CourseRepository]
 */
@Singleton
class CourseRepositoryImpl @Inject constructor(
    private val zfDataSource: ZfDataSource,
    private val localPreference: WeJhPreferenceDataSource,
    private val dao: CourseDao,
) : CourseRepository {

    override fun getCourses(year: Int, term: Term): Flow<List<Course>> =
        dao.getCourses(year, term).map { it.map(CourseEntity::asExternalModel) }

    override val lastSyncTimeStream: Flow<ZonedDateTime?> =
        localPreference.data.map {
            if (it.hasCoursesLastSyncTime())
                it.coursesLastSyncTime.toZonedDateTime()
            else null
        }

    override suspend fun sync(year: Int, term: Term) {
        val old = dao.getCourses(year, term).first()

        val classTable = zfDataSource.getClassTable(year.toString(), term.value).lessonsTable
        if (!classTable.isNullOrEmpty()) {
            val new = classTable.map { it.asLocalModel(year, term) }

            if (old.isEmpty())
                dao.insertCourses(new)
            else
                updateCourses(old, new)
        } else if (old.isNotEmpty()) {
            dao.deleteCourses(old)
        }

        localPreference.setCoursesLastSyncTime(ZonedDateTime.now())
    }

    // Insert new or updated elements and delete outdated elements
    private suspend fun updateCourses(old: List<CourseEntity>, new: List<CourseEntity>) {
        val toDelete = old.toMutableList()
        val toInsert = new.toMutableList()

        toDelete.removeIf { delete ->
            val index = toInsert.indexOfFirst { delete.equalsIgnoreId(it) }
            if (index != -1) {
                toInsert.removeAt(index)
                true
            } else false
        }

        if (toDelete.isEmpty() && toInsert.isEmpty()) return

        dao.deleteAndInsertCourses(toDelete, toInsert)
    }
}