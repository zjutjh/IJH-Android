package com.zjutjh.ijh.data

import com.zjutjh.ijh.data.local.CourseLocalDataSource
import com.zjutjh.ijh.data.remote.CourseRemoteDataSource
import com.zjutjh.ijh.mock.CourseRepositoryMock
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject

/**
 * Default impl of [CourseRepository]
 */
class CourseRepositoryImpl @Inject constructor(
    private val localDataSource: CourseLocalDataSource,
    private val remoteDataSource: CourseRemoteDataSource
) : CourseRepository {

    override fun getCourses(): ImmutableList<Course> {
        // TODO: unimplemented, just placeholder
        return CourseRepositoryMock().getCourses()
    }

}