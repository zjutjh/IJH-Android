package com.zjutjh.ijh.data

import com.zjutjh.ijh.data.local.CourseLocalDataSource
import com.zjutjh.ijh.data.remote.CourseNetworkDataSource
import com.zjutjh.ijh.mock.CourseRepositoryMock
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject

/**
 * Default impl of [CourseRepository]
 */
@ViewModelScoped
class CourseRepositoryImpl @Inject constructor(
    private val localDataSource: CourseLocalDataSource,
    private val remoteDataSource: CourseNetworkDataSource
) : CourseRepository {

    override fun getCourses(): ImmutableList<Course> {
        // TODO: unimplemented, just placeholder
        return CourseRepositoryMock().getCourses()
    }

}