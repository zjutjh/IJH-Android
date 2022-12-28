package com.zjutjh.ijh.module

import android.content.Context
import com.zjutjh.ijh.data.MyObjectBox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.objectbox.BoxStore

@Module
@InstallIn(SingletonComponent::class)
object ObjectBoxModule {

    @Provides
    fun provideBoxStore(@ApplicationContext context: Context): BoxStore {
        return MyObjectBox.builder()
            .androidContext(context)
            .build()
    }
}