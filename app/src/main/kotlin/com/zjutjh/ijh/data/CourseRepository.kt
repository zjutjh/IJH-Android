package com.zjutjh.ijh.data

import com.zjutjh.ijh.data.local.CourseLocalDataSource
import com.zjutjh.ijh.data.remote.CourseRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.DayOfWeek
import javax.inject.Inject

/**
 * Data layer Interface of [Course] repository
 */
interface CourseRepository {
    fun fetchCourses(): ImmutableList<Course>
}

/**
 * Default impl of [CourseRepository]
 */
class DefaultCourseRepository @Inject constructor(
    localDataSource: CourseLocalDataSource,
    remoteDataSource: CourseRemoteDataSource
) : CourseRepository {
    override fun fetchCourses(): ImmutableList<Course> {
        // TODO: unimplemented, just mock
        return persistentListOf(
            Course(
                "Design pattern in practice",
                "Mr. Info",
                "Software.A.302",
                "8:00-9:40",
                "1-16 Week | Mon (1-2) | 8:00-9:40",
                "4.0",
                Section(1, 2),
                DayOfWeek.MONDAY
            ), Course(
                "Software Engineering and Information Technology",
                "Mr. Hex",
                "Information.B.101",
                "9:55-11:35",
                "1-16 Week | Mon (1-2) | 8:00-9:40",
                "4.0",
                Section(1, 2),
                DayOfWeek.MONDAY
            )
        )
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class CourseModule {

    @Binds
    abstract fun bindCourseRepository(defaultCourseRepository: DefaultCourseRepository): CourseRepository

}