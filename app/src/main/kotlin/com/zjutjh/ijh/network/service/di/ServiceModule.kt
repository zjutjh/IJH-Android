package com.zjutjh.ijh.network.service.di

import android.util.Log
import com.zjutjh.ijh.BuildConfig
import com.zjutjh.ijh.network.adapter.ResultCallAdapterFactory
import com.zjutjh.ijh.network.di.DefaultOkHttpClient
import com.zjutjh.ijh.network.di.WeJhAuthOkHttpClient
import com.zjutjh.ijh.network.service.WeJhAuthorizedService
import com.zjutjh.ijh.network.service.WeJhService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    private fun retrofitCommonBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())

    @Provides
    @Singleton
    fun provideWeJhService(@DefaultOkHttpClient client: OkHttpClient): WeJhService {
        Log.i("ServiceModule", "provided WeJHService")

        return retrofitCommonBuilder()
            .baseUrl(BuildConfig.WE_JH_API_BASE_URL)
            .client(client)
            .build()
            .create(WeJhService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeJHAuthorizedService(
        @WeJhAuthOkHttpClient client: OkHttpClient
    ): WeJhAuthorizedService {
        Log.i("ServiceModule", "provided WeJHAuthorizedService")

        return retrofitCommonBuilder()
            .baseUrl(BuildConfig.WE_JH_API_BASE_URL)
            .client(client)
            .build()
            .create(WeJhAuthorizedService::class.java)
    }
}