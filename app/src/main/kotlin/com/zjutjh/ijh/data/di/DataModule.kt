package com.zjutjh.ijh.data.di

import com.zjutjh.ijh.data.repository.CourseRepository
import com.zjutjh.ijh.data.repository.CourseRepositoryImpl
import com.zjutjh.ijh.data.repository.WeJhUserRepository
import com.zjutjh.ijh.data.repository.WeJhUserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DataModule {

    @Binds
    fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository

    @Binds
    fun bindUserRepository(impl: WeJhUserRepositoryImpl): WeJhUserRepository
}