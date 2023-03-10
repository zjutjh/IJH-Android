package com.zjutjh.ijh.network.di

import com.zjutjh.ijh.network.cookie.WeJhCookieJar
import com.zjutjh.ijh.network.interceptor.ApiProcessInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun okHttpClientCommonBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .addInterceptor(ApiProcessInterceptor())
            .connectTimeout(Duration.ofSeconds(10))


    @Provides
    @Singleton
    @DefaultOkHttpClient
    fun provideDefaultOkHttpClient(): OkHttpClient =
        okHttpClientCommonBuilder().build()


    @Provides
    @Singleton
    @WeJhAuthorizedOkHttpClient
    fun provideWeJHAuthorizedOkHttpClient(cookieJar: WeJhCookieJar): OkHttpClient =
        okHttpClientCommonBuilder()
            .cookieJar(cookieJar)
            .build()
}