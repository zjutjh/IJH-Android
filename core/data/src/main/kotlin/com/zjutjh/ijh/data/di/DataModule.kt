package com.zjutjh.ijh.data.di

import com.zjutjh.ijh.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository

    @Binds
    fun bindWeJhUserRepository(impl: WeJhUserRepositoryImpl): WeJhUserRepository

    @Binds
    fun bindWeJhInfoRepository(impl: WeJhInfoRepositoryImpl): WeJhInfoRepository
}