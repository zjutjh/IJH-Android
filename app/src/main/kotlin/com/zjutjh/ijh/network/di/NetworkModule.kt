package com.zjutjh.ijh.network.di

import android.util.Log
import com.zjutjh.ijh.network.cookie.WeJhAuthorizationCookieJar
import com.zjutjh.ijh.network.interceptor.ApiProcessInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import java.time.Duration

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun okHttpClientCommonBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(ApiProcessInterceptor())
            .readTimeout(Duration.ofSeconds(5))
    }

    @Provides
    @DefaultOkHttpClient
    fun provideDefaultOkHttpClient(): OkHttpClient {
        Log.i("NetworkModule", "provided DefaultOkHttpClient")

        return okHttpClientCommonBuilder().build()
    }

    @Provides
    @WeJhAuthOkHttpClient
    fun provideWeJHAuthorizedOkHttpClient(cookieJar: WeJhAuthorizationCookieJar): OkHttpClient {
        Log.i("NetworkModule", "provided WeJHAuthOKHttpClient")

        return okHttpClientCommonBuilder()
            .cookieJar(cookieJar)
            .build()
    }
}