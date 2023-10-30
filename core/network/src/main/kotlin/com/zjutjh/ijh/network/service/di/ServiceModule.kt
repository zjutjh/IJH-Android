package com.zjutjh.ijh.network.service.di

import com.zjutjh.ijh.network.BuildConfig
import com.zjutjh.ijh.network.di.DefaultOkHttpClient
import com.zjutjh.ijh.network.di.WeJhAuthorizedOkHttpClient
import com.zjutjh.ijh.network.service.WeJhBasicService
import com.zjutjh.ijh.network.service.WeJhCardService
import com.zjutjh.ijh.network.service.WeJhUserService
import com.zjutjh.ijh.network.service.WeJhZfService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    private fun retrofitCommonBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())


    @Provides
    @Singleton
    fun provideWeJhBasicService(@DefaultOkHttpClient client: OkHttpClient): WeJhBasicService =
        retrofitCommonBuilder()
            .baseUrl(BuildConfig.WE_JH_API_BASE_URL)
            .client(client)
            .build()
            .create()


    @Provides
    @Singleton
    fun provideWeJhUserService(
        @WeJhAuthorizedOkHttpClient client: OkHttpClient
    ): WeJhUserService =
        retrofitCommonBuilder()
            .baseUrl(BuildConfig.WE_JH_API_BASE_URL + "user/")
            .client(client)
            .build()
            .create()

    @Provides
    @Singleton
    fun provideWeJhZfService(
        @WeJhAuthorizedOkHttpClient client: OkHttpClient
    ): WeJhZfService =
        retrofitCommonBuilder()
            .baseUrl(BuildConfig.WE_JH_API_BASE_URL + "func/zf/")
            .client(client)
            .build()
            .create()

    @Provides
    @Singleton
    fun provideWeJhCardService(
        @WeJhAuthorizedOkHttpClient client: OkHttpClient
    ): WeJhCardService =
        retrofitCommonBuilder()
            .baseUrl(BuildConfig.WE_JH_API_BASE_URL + "func/card/")
            .client(client)
            .build()
            .create()
}