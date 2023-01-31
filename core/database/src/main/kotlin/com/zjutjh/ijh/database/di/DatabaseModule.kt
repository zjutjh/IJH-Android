package com.zjutjh.ijh.database.di

import android.content.Context
import androidx.room.Room
import com.zjutjh.ijh.database.IJhDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideIJhDatabase(
        @ApplicationContext context: Context
    ): IJhDatabase = Room.databaseBuilder(
        context,
        IJhDatabase::class.java,
        "ijh.db"
    ).build()
}