package com.zjutjh.ijh.data.di

import com.zjutjh.ijh.data.CampusRepository
import com.zjutjh.ijh.data.CardRepository
import com.zjutjh.ijh.data.CourseRepository
import com.zjutjh.ijh.data.ElectricityRepository
import com.zjutjh.ijh.data.WeJhUserRepository
import com.zjutjh.ijh.data.impl.CampusRepositoryImpl
import com.zjutjh.ijh.data.impl.CardRepositoryImpl
import com.zjutjh.ijh.data.impl.CourseRepositoryImpl
import com.zjutjh.ijh.data.impl.ElectricityRepositoryImpl
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
    fun bindCampusRepository(impl: CampusRepositoryImpl): CampusRepository

    @Binds
    fun bindCardRepository(impl: CardRepositoryImpl): CardRepository

    @Binds
    fun bindElectricityRepository(impl: ElectricityRepositoryImpl): ElectricityRepository
}