package com.zjutjh.ijh.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.zjutjh.ijh.datastore.UserPreference
import com.zjutjh.ijh.datastore.UserPreferenceSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideWeJhUserPreferenceDataStore(
        @ApplicationContext context: Context,
        serializer: UserPreferenceSerializer
    ): DataStore<UserPreference> =
        DataStoreFactory.create(
            serializer,
        ) {
            context.dataStoreFile("user_preference.pb")
        }
}