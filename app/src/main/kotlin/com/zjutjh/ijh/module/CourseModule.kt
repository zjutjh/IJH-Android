package com.zjutjh.ijh.module

import com.zjutjh.ijh.data.CourseRepositoryImpl
import com.zjutjh.ijh.data.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CourseModule {

    @Binds
    abstract fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository

}