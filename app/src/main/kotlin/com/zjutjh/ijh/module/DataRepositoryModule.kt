package com.zjutjh.ijh.module

import com.zjutjh.ijh.data.CourseRepository
import com.zjutjh.ijh.data.CourseRepositoryImpl
import com.zjutjh.ijh.data.WeJHUserRepository
import com.zjutjh.ijh.data.WeJHUserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataRepositoryModule {

    @Binds
    abstract fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository

    @Binds
    abstract fun bindUserRepository(impl: WeJHUserRepositoryImpl): WeJHUserRepository

}