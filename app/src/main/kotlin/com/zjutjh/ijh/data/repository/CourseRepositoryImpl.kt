package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.data.model.Course
import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject

/**
 * Default impl of [CourseRepository]
 */
@ViewModelScoped
class CourseRepositoryImpl @Inject constructor(
) : CourseRepository {

    override suspend fun getCourses(): ImmutableList<Course> {
        // TODO: unimplemented, just placeholder
        return CourseRepositoryMock().getCourses()
    }
}