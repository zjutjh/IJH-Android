package com.zjutjh.ijh.data.repository

import android.util.Log
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.converter.toZonedDateTime
import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.network.ZfDataSource
import com.zjutjh.ijh.network.model.asExternalModel
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
) : CourseRepository {

    override val lastSyncTimeStream: Flow<ZonedDateTime?> =
        localPreference.data.map {
            if (it.hasCoursesLastSyncTime())
                it.coursesLastSyncTime.toZonedDateTime()
            else null
        }

    override suspend fun sync(year: Int, term: Term) {
        val classTable = zfDataSource.getClassTable(year.toString(), term.value).lessonsTable
        if (classTable != null) {
            // TODO: store into Database.
            Log.i("CourseSync", classTable.map { it.asExternalModel() }.toString())
            localPreference.setCoursesLastSyncTime(ZonedDateTime.now())
        }
    }
}