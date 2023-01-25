package com.zjutjh.ijh.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.zjutjh.ijh.datastore.WeJhPreferenceSerializer
import com.zjutjh.ijh.datastore.model.WeJhPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val WE_JH_FILENAME = "we_jh.pb"

    @Provides
    @Singleton
    fun provideWeJhDataStore(
        @ApplicationContext context: Context,
        serializer: WeJhPreferenceSerializer
    ): DataStore<WeJhPreference> =
        DataStoreFactory.create(
            serializer,
        ) {
            context.dataStoreFile(WE_JH_FILENAME)
        }
}