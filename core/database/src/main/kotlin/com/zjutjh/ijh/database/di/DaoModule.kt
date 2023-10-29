package com.zjutjh.ijh.database.di

import com.zjutjh.ijh.database.IJhDatabase
import com.zjutjh.ijh.database.dao.CourseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideCourseDao(database: IJhDatabase): CourseDao = database.courseDao()
}