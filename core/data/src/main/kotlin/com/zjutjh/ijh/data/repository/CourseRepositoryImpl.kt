package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.data.model.asLocalModel
import com.zjutjh.ijh.database.dao.CourseDao
import com.zjutjh.ijh.database.model.CourseEntity
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.toZonedDateTime
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.network.ZfDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * Default impl of [CourseRepository]
 */
@ActivityRetainedScoped
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
        val classTable = zfDataSource.getClassTable(year.toString(), term.value).lessonsTable
        if (classTable != null) {
            dao.deleteCourses(year, term)
            dao.insertCourses(classTable.map { it.asLocalModel(year, term) })
            localPreference.setCoursesLastSyncTime(ZonedDateTime.now())
        }
    }
}