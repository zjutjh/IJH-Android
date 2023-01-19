package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.data.repository.mock.CourseRepositoryMock
import com.zjutjh.ijh.model.Course
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