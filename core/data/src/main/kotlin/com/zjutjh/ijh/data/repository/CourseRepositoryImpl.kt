package com.zjutjh.ijh.data.repository

import android.util.Log
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.model.Course
import com.zjutjh.ijh.network.WeJhZfDataSource
import com.zjutjh.ijh.network.model.asExternalModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.collections.immutable.ImmutableList
import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * Default impl of [CourseRepository]
 */
@ViewModelScoped
class CourseRepositoryImpl @Inject constructor(
    private val zfDataSource: WeJhZfDataSource,
    private val localPreference: WeJhPreferenceDataSource,
) : CourseRepository {

    override suspend fun getCourses(): ImmutableList<Course> {
        // TODO: unimplemented, just placeholder
        return CourseRepositoryMock().getCourses()
    }

    override suspend fun sync() {
        val classTable = zfDataSource.getClassTable("2022", "上")
        if (classTable.lessonsTable != null) {
            // TODO: store into Database.
            Log.i("CourseSync", classTable.lessonsTable!!.map { it.asExternalModel() }.toString())
            localPreference.setCoursesLastSyncTime(ZonedDateTime.now())
        }
    }
}