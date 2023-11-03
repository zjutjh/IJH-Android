package com.zjutjh.ijh.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.zjutjh.ijh.datastore.WeJhPreferenceSerializer
import com.zjutjh.ijh.datastore.model.IJhPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val IJH_FILENAME = "ijh.pb"

    @Provides
    @Singleton
    fun provideIJhDataStore(
        @ApplicationContext context: Context,
        serializer: WeJhPreferenceSerializer
    ): DataStore<IJhPreference> =
        DataStoreFactory.create(
            serializer,
        ) {
            context.dataStoreFile(IJH_FILENAME)
        }
}