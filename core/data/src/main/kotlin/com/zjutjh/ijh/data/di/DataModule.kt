package com.zjutjh.ijh.data.di

import com.zjutjh.ijh.data.CourseRepository
import com.zjutjh.ijh.data.WeJhInfoRepository
import com.zjutjh.ijh.data.WeJhUserRepository
import com.zjutjh.ijh.data.impl.CourseRepositoryImpl
import com.zjutjh.ijh.data.impl.WeJhInfoRepositoryImpl
import com.zjutjh.ijh.data.impl.WeJhUserRepositoryImpl
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