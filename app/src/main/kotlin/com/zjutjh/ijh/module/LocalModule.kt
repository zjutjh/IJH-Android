package com.zjutjh.ijh.module

import android.content.Context
import android.util.Log
import com.zjutjh.ijh.data.MyObjectBox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideBoxStore(@ApplicationContext context: Context): BoxStore {
        Log.i("Module", "provided BoxStore")

        return MyObjectBox.builder()
            .androidContext(context)
            .build()
    }
}